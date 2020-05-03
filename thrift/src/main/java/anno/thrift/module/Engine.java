package anno.thrift.module;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Engine {
    /**
     * 转发器
     * @param input 表单数据
     * @return
     * @throws Exception
     */
    public static ActionResult Transmit(Map<String, String> input) throws Exception {
        //①通过类装载器获取Car类对象
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        StringBuilder sb = new StringBuilder();
        sb.append(input.get(Eng.NAMESPACE));
        sb.append("service");
        sb.append(".");
        sb.append(input.get(Eng.CLASS));
        sb.append("Module");

        Class clazz = loader.loadClass(sb.toString());
        //②获取类的默认构造器对象并通过它实例化Car
        Constructor cons = clazz.getDeclaredConstructor((Class[]) null);
        Object module = cons.newInstance();
        Method init = clazz.getMethod("Init", HashMap.class);
        boolean initRlt = (boolean) init.invoke(module, input);
        if (initRlt) {
            ActionResult rlt;
            try{
                Method processMethod = GetRequestMethod(clazz, input);
                Object[] objs = GetRequestInfo(processMethod, input);
                rlt=(ActionResult) processMethod.invoke(module, objs);
            }
            catch (Exception ex){
                rlt=  new ActionResultObject(false, ex, null, "Invoke Failure.");
            }

            return rlt;

        } else {
            return new ActionResultObject(false, null, null, "Init Failure.");
        }

    }

    /**
     * 获取业务方法需要的参数
     * @param method
     * @param input
     * @return
     */
    private static Object[] GetRequestInfo(Method method, Map<String, String> input) {
        Parameter[] params = method.getParameters();
        Object[] objs = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            String typeName = params[i].getType().getName();
            if (input.containsKey(params[i].getName())) {
                if (!typeName.startsWith("java.lang.")&&!typeName.equals("int")) {//不是基本类型
                    objs[i] = JSON.parseObject(input.get(params[i].getName()),params[i].getType());
                } else if (typeName.equals(String.class)) {
                    objs[i] = input.get(params[i].getName());
                } else if (typeName.equals(Integer.class)) {
                    objs[i] = Integer.parseInt(input.get(params[i].getName()));
                }  else if (typeName.equals(int.class)) {
                    objs[i] = (int)Integer.parseInt(input.get(params[i].getName()));
                } else if (typeName.equals(Long.class)) {
                    objs[i] = Long.parseLong(input.get(params[i].getName()));
                } else if (typeName.equals(Double.class)) {
                    objs[i] = Double.parseDouble(input.get(params[i].getName()));
                } else if (typeName.equals(Float.class)) {
                    objs[i] = Float.parseFloat(input.get(params[i].getName()));
                } else if (typeName.equals(Character.class)) {
                    objs[i] = input.get(params[i].getName()).charAt(0);
                } else if (typeName.equals(Short.class)) {
                    objs[i] = Short.parseShort(input.get(params[i].getName()));
                } else if (typeName.equals(Boolean.class)) {
                    objs[i] = Boolean.parseBoolean(input.get(params[i].getName()));
                } else {
                    objs[i] = input.get(params[i].getName());
                }
            } else {
                objs[i] = null;
            }
        }
        return objs;
    }

    /**
     * 获取处理方法
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
        if (className.equals(Integer.class) ||
                className.equals(Byte.class) ||
                className.equals(Long.class) ||
                className.equals(Double.class) ||
                className.equals(Float.class) ||
                className.equals(Character.class) ||
                className.equals(Short.class) ||
                className.equals(Boolean.class)) {
            return true;
        }
        return false;
    }
}
