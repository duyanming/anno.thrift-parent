package dynamicProxy;

public class dynamicProxyTest {
    public  void   request1(){
        AnnoProxyBuilder builder=new AnnoProxyBuilder();
        IUserDao userDao=builder.getService(IUserDao.class);
        System.out.println(userDao.getUserName());
    }
}
