package anno.thrift.imp;

import anno.thrift.client.Connector;
import anno.thrift.rpc.BrokerService;
import anno.thrift.module.ActionResult;
import anno.thrift.module.Engine;
import com.alibaba.fastjson.JSON;

import java.util.Map;

public class BrokerServiceImp implements BrokerService.Iface {

  @Override
  public String broker(Map<String, String> input) {
    try {
      ActionResult rlt = Engine.Transmit(input);
      String output = JSON.toJSONStringWithDateFormat(rlt, "yyyy-MM-dd HH:mm:ss.SSS");
      return output;
    } catch (Exception ex) {
      return Connector.FailMessage(ex.getMessage());
    }
  }
}
