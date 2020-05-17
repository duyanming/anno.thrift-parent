package anno.thrift.module;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface EnginStrategy {
     Object GetProcessInstance(Class clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
