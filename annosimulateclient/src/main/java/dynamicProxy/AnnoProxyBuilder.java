package dynamicProxy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

/** @author admin */
public class AnnoProxyBuilder {

  public <TInterface> TInterface getService(Class<TInterface> cls) {
    AnnoInvocationHandler invocationHandler = new AnnoInvocationHandler();
    Object newProxyInstance =
        Proxy.newProxyInstance(cls.getClassLoader(), new Class[] {cls}, invocationHandler);
    return (TInterface) newProxyInstance;
  }
}
