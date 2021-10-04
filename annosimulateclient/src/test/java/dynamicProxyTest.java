import anno.thrift.dynamicProxy.AnnoProxyBuilder;

public class dynamicProxyTest {
    public static void main(String[] args) {
        AnnoProxyBuilder builder=new AnnoProxyBuilder();
        IUserDao userDao=builder.getService(IUserDao.class);
        System.out.println(userDao.sayHi("Anno"));
    }
}
