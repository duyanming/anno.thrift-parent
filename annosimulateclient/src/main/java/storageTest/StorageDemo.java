package storageTest;

import anno.thrift.server.ServerInfo;
import anno.thrift.storage.AnnoData;
import anno.thrift.storage.AnnoDataResult;
import anno.thrift.storage.CONST;
import anno.thrift.storage.StorageEngine;
import com.alibaba.fastjson.JSON;
import org.apache.thrift.TException;

import java.util.HashMap;

public class StorageDemo {
  private ServerInfo serverInfo;

  public void Request() throws TException {
    if (serverInfo == null) {
      serverInfo = ServerInfo.getDefault();
      serverInfo.setPort(6660);
      serverInfo.setLocalAddress("127.0.0.1");
    }
    HashMap<String, String> input = new HashMap<>();
    input.put(CONST.Opt, CONST.Upsert);

    AnnoData annoData = new AnnoData();
    annoData.setApp(serverInfo.getAppName());
    annoData.setId("StorageDemoId");
    annoData.setValue("StorageDemo Value");
    input.put(CONST.Data, JSON.toJSONString(annoData));
    String rlt = StorageEngine.Invoke(input);
    System.out.println(rlt);

    input.clear();
    input.put(CONST.Opt, CONST.FindById);
    input.put(CONST.Id, "StorageDemoId");
    AnnoDataResult<AnnoData> objRlt= StorageEngine.InvokeObj(input, new AnnoDataResult<AnnoData>().getClass());
    System.out.println(objRlt);
  }
}
