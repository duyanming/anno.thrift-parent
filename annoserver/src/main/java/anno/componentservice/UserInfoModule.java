package anno.componentservice;

import anno.componentservice.Models.UserInfo;
import anno.componentservice.events.UserEvent;
import anno.configuration.AnnoTheadPool;
import anno.entities.SysMember;
import anno.repository.SysMemberMapper;
import anno.thrift.annotation.AnnoInfo;
import anno.thrift.module.ActionResult;
import anno.thrift.module.BaseModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
@Service
//@Scope("prototype")
public class UserInfoModule extends BaseModule {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private  SysMemberMapper sysMemberMapper;
    @AnnoInfo(desc = "用户信息")
    public ActionResult<Object> GetUserInfo(GetUserInfoRequestDto queryInput){
        UserInfo userinfo=new UserInfo();
        userinfo.setAge(18);
        userinfo.setName("Tom");
        HashMap<String,Object> output=new HashMap<String, Object>();
        output.put("key1","value1");
        output.put("key2","value2");
        for(HashMap.Entry<String,Object> kv:output.entrySet()){
            output.put(kv.getKey(),kv.getValue());
        }
        HashMap<String,Object> outputData=new HashMap<String, Object>();
        outputData.put("queryInput",queryInput);
        outputData.put("userinfo",userinfo);
       String msg= "this message from Java Server UserInfoModule.";
        return new ActionResult<Object>(true, outputData, output, msg);
    }
    @AnnoInfo(desc = "你好世界")
    public ActionResult<Object> HelloWorld(@AnnoInfo(desc = "名称",name = "name",required = false,defaultValue = "Anno Default Value") String anno){
        String greetings="Hello "+anno+" I am Anno!";
        return  new ActionResult<>(true,greetings);
    }
    public void   PublishMsg(String name){
        if(name==null){
            name="Anno";
        }
        UserEvent uv=new UserEvent();
        uv.setId(10010);
        uv.setName(name);
        /**
         * 线程池方式发布事件
         */
        AnnoTheadPool.getPool().execute(()->{
            publisher.publishEvent(uv);
        });
/**
 * 发布事件异步 创建线程
 */
//        new Thread(()->{
//            publisher.publishEvent(uv);
//        }).start();
        /**
         * 直接发布事件 同步
         */
//        publisher.publishEvent(uv);
    }

    @AnnoInfo(desc = "根据ID获取用户信息")
    public ActionResult<SysMember> GetUserAutowired(long id) {
        SysMember member=sysMemberMapper.selectById(id);
        return new ActionResult<>(true,member);
    }
}
