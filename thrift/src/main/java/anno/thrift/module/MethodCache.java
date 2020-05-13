package anno.thrift.module;

import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Data
public class MethodCache {
    private Constructor constructor;
    private Method process;
    private Method init;
    private Parameter[] parameters;
}
