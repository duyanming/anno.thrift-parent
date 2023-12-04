import anno.thrift.client.DefaultConfigManager;
import anno.thrift.dynamicProxy.AnnoProxyBuilder;
import org.apache.thrift.TException;

public class dynamicProxyTest {
    public static void main(String[] args) throws TException {
        DefaultConfigManager.SetDefaultConnectionPool(500, 4, 50);
        DefaultConfigManager.SetDefaultConfiguration("AnnoClient", "127.0.0.1", 7010);

        AnnoProxyBuilder builder=new AnnoProxyBuilder();
//        IUserDao userDao=builder.getService(IUserDao.class);
//        System.out.println(userDao.sayHi("Anno"));
        IExamModule examModule=builder.getService(IExamModule.class);
        System.out.println(examModule.sayHi("Anno"));
    }
}
