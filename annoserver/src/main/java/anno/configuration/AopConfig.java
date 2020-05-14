package anno.configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopConfig {
    private static ApplicationContext context;
    public static ApplicationContext Default(){
        if(context==null){
            context=new ClassPathXmlApplicationContext("applicationContext.xml") ;
        }
       return context;
    }
    public void Init(){
//        context.getBean(AdviceManager.class);
    }
}
