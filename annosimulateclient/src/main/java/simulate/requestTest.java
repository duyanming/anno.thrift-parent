package simulate;

import anno.thrift.client.Connector;
import anno.thrift.client.DefaultConfigManager;
import anno.thrift.module.ActionResult;
import anno.thrift.module.Eng;
import com.alibaba.fastjson.JSON;
import org.apache.thrift.TException;
import simulate.dto.GetUserInfoRequestDto;

import java.util.HashMap;
import java.util.Map;

public class requestTest {
    public void Request1() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put(Eng.NAMESPACE,"anno.component");
        input.put(Eng.CLASS,"UserInfo");
        input.put(Eng.METHOD,"GetUserInfo");
        GetUserInfoRequestDto queryInput=new GetUserInfoRequestDto();
        queryInput.setAge(31);
        queryInput.setQuery("annosimulateclient");
        input.put("queryInput", JSON.toJSONString(queryInput));
        ActionResult<HashMap<String,Object>> actionResult = Connector.InvokeObj(input);
        System.out.println(actionResult);
    }
    public void Request1AnnoNet(Integer id) throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put(Eng.NAMESPACE,"Anno.Plugs.Logic");
        input.put(Eng.CLASS,"Joke");
        input.put(Eng.METHOD,"Test1");
        input.put("id", id.toString());
        ActionResult<String> actionResult = Connector.InvokeObj(input);
        System.out.println(actionResult);
    }

    public void RequestParam() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put(Eng.NAMESPACE,"anno.component");
        input.put(Eng.CLASS,"UserInfo");
        input.put(Eng.METHOD,"GetUserAutowired");
        input.put("id","299935790530562");
        ActionResult<HashMap<String,Object>> actionResult = Connector.InvokeObj(input);
        System.out.println(actionResult);
    }
}
