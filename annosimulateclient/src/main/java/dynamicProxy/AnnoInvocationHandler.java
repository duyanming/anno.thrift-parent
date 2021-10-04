package dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author admin
 */
public class AnnoInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果传进来是一个已实现的具体类
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            return run(method, args);
        }
        return null;
    }

    /**
     * 实现接口的核心方法
     *
     * @param method
     * @param args
     * @return
     */
    public Object run(Method method, Object[] args) {
        //TODO
        //如远程http调用
        //如远程方法调用（rmi)
        //....
        System.out.println(method.getName());
        System.out.println("ParameterCount"+method.getParameterCount());
        for (Object obj : args) {
            System.out.println("arg:"+obj.toString());
        }
        return "method call success!";
    }
}
