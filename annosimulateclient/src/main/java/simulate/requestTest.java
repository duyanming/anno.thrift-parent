package simulate;

import anno.thrift.client.Connector;
import anno.thrift.client.DefaultConfigManager;
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
        String rlt = Connector.Invoke(input);
        System.out.printf(rlt);
    }
}
