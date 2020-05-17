package anno.thrift.module;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DefaultEnginStrategy implements EnginStrategy {
    @Override
    public Object GetProcessInstance(Class clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor cons = clazz.getDeclaredConstructor((Class[]) null);
        Object instance= cons.newInstance();
        return  instance;
    }
}
