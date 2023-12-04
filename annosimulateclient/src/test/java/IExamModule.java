import anno.thrift.dynamicProxy.AnnoProxy;

@AnnoProxy(channel="Anno.Plugs.Viper",router="Exam")
public interface IExamModule {
    @AnnoProxy(method = "SayHi")
    public String sayHi(String name);
}
