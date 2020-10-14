package anno.thrift.imp;

import anno.thrift.client.Connector;
import anno.thrift.module.Eng;
import anno.thrift.rpc.BrokerService;
import anno.thrift.module.ActionResult;
import anno.thrift.module.Engine;
import anno.thrift.sysInfo.ServerStatus;
import anno.thrift.sysInfo.UseSysInfoWatch;
import com.alibaba.fastjson.JSON;

import java.util.Map;

public class BrokerServiceImp implements BrokerService.Iface {

  @Override
  public String broker(Map<String, String> input) {
    try {
        if((input.getOrDefault(Eng.NAMESPACE,"").equals("Anno.Plugs.Trace")
                &&input.getOrDefault(Eng.CLASS,"").equals("Trace")
                &&input.getOrDefault(Eng.METHOD,"").equals("GetServerStatus")
        )||(input.getOrDefault(Eng.NAMESPACE,"").equals("Anno.Plugs.Monitor")
                &&input.getOrDefault(Eng.CLASS,"").equals("Resource")
                &&input.getOrDefault(Eng.METHOD,"").equals("GetServerStatus")
        )){

            return JSON.toJSONStringWithDateFormat(new ActionResult<ServerStatus>(true,UseSysInfoWatch.GetServerStatus()), "yyyy-MM-dd HH:mm:ss.SSS");
        }
      ActionResult rlt = Engine.Transmit(input);
      String output = JSON.toJSONStringWithDateFormat(rlt, "yyyy-MM-dd HH:mm:ss.SSS");
      return output;
    } catch (Exception ex) {
      return Connector.FailMessage(ex.getMessage());
    }
  }
}
