package anno.thrift.imp;

import anno.thrift.rpc.BrokerService;
import anno.thrift.module.ActionResult;
import anno.thrift.module.Engine;
import com.alibaba.fastjson.JSON;

import java.util.Map;

public class BrokerServiceImp implements BrokerService.Iface {

    @Override
    public String broker(Map<String, String> input) throws Throwable {
        ActionResult rlt = Engine.Transmit(input);
        String output = JSON.toJSONString(rlt);
        return output;
    }
}
