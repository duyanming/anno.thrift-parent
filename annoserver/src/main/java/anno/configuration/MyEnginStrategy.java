package anno.configuration;


public class MyEnginStrategy implements anno.thrift.module.EnginStrategy {
    @Override
    public Object GetProcessInstance(Class clazz){
       return AopConfig.Default().getBean(clazz);
    }
}
