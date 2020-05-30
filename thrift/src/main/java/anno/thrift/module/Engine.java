package anno.thrift.module;

import anno.thrift.annotation.AnnoParam;
import anno.thrift.exception.AnnoArgumentNullException;
import com.alibaba.fastjson.JSON;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class Engine {

  public static EnginStrategy enginStrategy = new DefaultEnginStrategy();
  private static ConcurrentHashMap<String, MethodCache> processMethods = new ConcurrentHashMap<>();
  /**
   * 转发器
   *
   * @param input 表单数据
   * @return
   * @throws Exception
   */
  public static ActionResult Transmit(Map<String, String> input) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append(input.get(Eng.NAMESPACE));
    sb.append("service");
    sb.append(".");
    sb.append(input.get(Eng.CLASS));
    sb.append("Module");

    String key=sb.toString()+input.get(Eng.METHOD);
    MethodCache mc = processMethods.get(key);
    if (mc != null) {
      return InvokeFromCache(mc, input);
    }
    // ①通过类装载器获取 Process类对象
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Class clazz = loader.loadClass(sb.toString());
    // ②获取类的默认构造器对象并通过它实例化Car
    Constructor cons = clazz.getDeclaredConstructor((Class[]) null);
    Object module =enginStrategy.GetProcessInstance(clazz);
    Method init = clazz.getMethod("Init", HashMap.class);
    boolean initRlt = (boolean) init.invoke(module, input);
    if (initRlt) {
      ActionResult rlt;
      try {
        Method processMethod = GetRequestMethod(clazz, input);
        if (processMethod == null) {

          return new ActionResultObject(
              false, null, null, "Not Found Action:" + input.get(Eng.METHOD));
        }
        Object[] objs = GetRequestInfo(processMethod, input);
        rlt = (ActionResult) processMethod.invoke(module, objs);
        new Thread(()->{
         MethodCache methodCache= new MethodCache();
          methodCache.setClazz(clazz);
          methodCache.setConstructor(cons);
          methodCache.setInit(init);
          methodCache.setProcess(processMethod);
          methodCache.setParameters(processMethod.getParameters());
          processMethods.putIfAbsent(key,methodCache);
        }).start();
      } catch (Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        rlt =
                new ActionResultObject(
                        false,
                        null,
                        null,
                        "Invoke Failure. \n Msg:"
                                + sw.toString());
      }

      return rlt;

    } else {
      return new ActionResultObject(false, null, null, "Init Failure.");
    }
  }

  /**
   * 获取业务方法需要的参数
   *
   * @param method
   * @param input
   * @return
   */
  private static Object[] GetRequestInfo(Method method, Map<String, String> input)
      throws AnnoArgumentNullException {
    Parameter[] params = method.getParameters();
    return GetRequestInfo(params, input);
  }
  /**
   * 获取业务方法需要的参数
   *
   * @param params
   * @param input
   * @return
   */
  private static Object[] GetRequestInfo(Parameter[] params, Map<String, String> input)
      throws AnnoArgumentNullException {
    Object[] objs = new Object[params.length];
    for (int i = 0; i < params.length; i++) {
      String typeName = params[i].getType().getName();
      String paramName = params[i].getName();
      AnnoParam annotation = params[i].getAnnotation(AnnoParam.class);
      if (annotation != null) {
        if (annotation.name() != "") {
          paramName = annotation.name();
        }
        if (annotation.required() && input.containsKey(paramName) == false) {
          throw new AnnoArgumentNullException("Parameter " + paramName + " cannot be null");
        }
        if (annotation.required() == false && input.containsKey(paramName) == false) {
          input.put(paramName, annotation.defaultValue());
        }
      }
      if (input.containsKey(paramName)) {
        String Value = input.get(paramName);
        if (!typeName.startsWith("java.lang.") && !typeName.equals("int")) { // 不是基本类型
          objs[i] = JSON.parseObject(Value, params[i].getType());
        } else if (typeName.equals(String.class)) {
          objs[i] = Value;
        } else if (typeName.equals(Integer.class)) {
          objs[i] = Integer.parseInt(Value);
        } else if (typeName.equals(int.class)) {
          objs[i] = (int) Integer.parseInt(Value);
        } else if (typeName.equals(Long.class)) {
          objs[i] = Long.parseLong(Value);
        } else if (typeName.equals(Double.class)) {
          objs[i] = Double.parseDouble(Value);
        } else if (typeName.equals(Float.class)) {
          objs[i] = Float.parseFloat(Value);
        } else if (typeName.equals(Character.class)) {
          objs[i] = Value.charAt(0);
        } else if (typeName.equals(Short.class)) {
          objs[i] = Short.parseShort(Value);
        } else if (typeName.equals(Boolean.class)) {
          objs[i] = Boolean.parseBoolean(Value);
        } else {
          objs[i] = Value;
        }
      } else {
        objs[i] = null;
      }
    }
    return objs;
  }
  /**
   * 获取处理方法
   *
   * @param clazz
   * @param input
   * @return
   */
  private static Method GetRequestMethod(Class clazz, Map<String, String> input) {
    Method processMethod = null;
    for (Method method : clazz.getMethods()) {
      if (input.get(Eng.METHOD).equals(method.getName())) {
        processMethod = method;
        break;
      }
    }
    return processMethod;
  }

  /**
   * 判断object是否为基本类型
   *
   * @param object
   * @return
   */
  public static boolean isBaseType(Object object) {
    Class className = object.getClass();
    if (className.equals(Integer.class)
        || className.equals(Byte.class)
        || className.equals(Long.class)
        || className.equals(Double.class)
        || className.equals(Float.class)
        || className.equals(Character.class)
        || className.equals(Short.class)
        || className.equals(Boolean.class)) {
      return true;
    }
    return false;
  }

  private static ActionResult InvokeFromCache(MethodCache mc, Map<String, String> input)
          throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Object module = enginStrategy.GetProcessInstance(mc.getClazz());
    boolean initRlt = (boolean) mc.getInit().invoke(module, input);
    if (initRlt) {
      ActionResult rlt;
      try {
        Object[] objs = GetRequestInfo(mc.getParameters(), input);
        rlt = (ActionResult) mc.getProcess().invoke(module, objs);
      }
      catch (Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        rlt =
            new ActionResultObject(
                false,
                null,
                null,
                "Invoke Failure. \n Msg:"
                    + sw.toString());
      }

      return rlt;

    } else {
      return new ActionResultObject(false, null, null, "Init Failure.");
    }
  }
}
