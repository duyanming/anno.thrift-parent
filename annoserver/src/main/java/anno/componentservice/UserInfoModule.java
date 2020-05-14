package anno.componentservice;

import anno.componentservice.Models.UserInfo;
import anno.componentservice.events.UserEvent;
import anno.thrift.annotation.AnnoParam;
import anno.thrift.module.ActionResult;
import anno.thrift.module.BaseModule;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
@Service
//@Scope("prototype")
public class UserInfoModule extends BaseModule {
    @Resource
    private ApplicationEventPublisher publisher;
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
    public ActionResult<Object> HelloWorld(@AnnoParam(name = "name",required = false,defaultValue = "Anno Default Value") String anno){
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
        publisher.publishEvent(uv);

    }
}
