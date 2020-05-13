package anno.componentservice;

import anno.componentservice.Models.UserInfo;
import anno.thrift.annotation.AnnoParam;
import anno.thrift.module.ActionResult;
import anno.thrift.module.BaseModule;

import java.util.HashMap;

public class UserInfoModule extends BaseModule {
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
}
