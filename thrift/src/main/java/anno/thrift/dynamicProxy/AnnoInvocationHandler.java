package anno.thrift.dynamicProxy;

import anno.thrift.annotation.AnnoInfo;
import anno.thrift.client.Connector;
import anno.thrift.exception.AnnoArgumentNullException;
import anno.thrift.module.ActionResult;
import anno.thrift.module.ActionResultObject;
import anno.thrift.module.Eng;
import anno.thrift.module.Engine;
import anno.thrift.sysInfo.ServerStatus;
import anno.thrift.sysInfo.UseSysInfoWatch;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 */
public class AnnoInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果传进来是一个已实现的具体类
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            return run(method, args);
        }
    }

    /**
     * 实现接口的核心方法
     *
     * @param method
     * @param args
     * @return
     */
    public Object run(Method method, Object[] args) throws Exception {
        String rltStr = null;
        //region Invoke
        String _package = "";
        String _class = method.getDeclaringClass().getName();
        String _method = method.getName();

        AnnoProxy annoProxy = method.getDeclaringClass().getAnnotation(AnnoProxy.class);
        if (annoProxy != null) {
            if (annoProxy.channel() != null && !annoProxy.channel().equals("")) {
                _package = annoProxy.channel();
            }
            if (annoProxy.router() != null && !annoProxy.router().equals("")) {
                _class = annoProxy.router();
            }
            if (annoProxy.method() != null && !annoProxy.method() .equals("")) {
                _method = annoProxy.method();
            }
        }
        annoProxy = method.getAnnotation(AnnoProxy.class);
        if (annoProxy != null) {
            if (annoProxy.channel() != null && !annoProxy.channel().equals("")) {
                _package = annoProxy.channel();
            }
            if (annoProxy.router() != null && !annoProxy.router().equals("")) {
                _class = annoProxy.router();
            }
            if (annoProxy.method() != null && !annoProxy.method() .equals("")) {
                _method = annoProxy.method();
            }
        }
        Map<String, String> input = new HashMap<>();
        input.put(Eng.NAMESPACE, _package);
        input.put(Eng.CLASS, _class);
        input.put(Eng.METHOD, _method);
        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            Parameter param = params[i];
            String typeName = param.getType().getName();
            String paramName = param.getName();
            AnnoInfo annotation = param.getAnnotation(AnnoInfo.class);
            if (annotation != null) {
                if (annotation.name() != null && annotation.name() != "") {
                    paramName = annotation.name();
                }
            }
            String value = getValue(args[i], typeName);
            input.put(paramName, value);
        }
        rltStr= Connector.Invoke(input);
        //endregion Invoke
        Class<?> clazz = method.getReturnType();
        if(clazz.isAssignableFrom(ActionResult.class)){
            return  JSON.parseObject(rltStr,clazz);
        }else{
            ActionResultObject actionResultObject  =JSON.parseObject(rltStr,ActionResultObject.class);
            return  JSON.parseObject(JSON.toJSONString(actionResultObject.getOutputData()),clazz);
        }
    }

    private String getValue(Object val, String typeName) {
        if (val == null) {
            return "";
        }
        if (!typeName.startsWith("java.lang.")
                && !typeName.equals("int")
                && !typeName.equals("long")
                && !typeName.equals("short")
                && !typeName.equals("boolean")
                && !typeName.equals("char")
        ) { // 不是基本类型
            return JSON.toJSONString(val);
        } else {
            return val.toString();
        }
    }
}
