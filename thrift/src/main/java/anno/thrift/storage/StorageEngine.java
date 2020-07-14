package anno.thrift.storage;

import anno.thrift.rpc.BrokerCenter;
import anno.thrift.server.ServerInfo;
import com.alibaba.fastjson.JSON;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.HashMap;

public  class StorageEngine {
    public static String Invoke(HashMap<String, String> input) throws TException {
        ServerInfo serverInfo = ServerInfo.getDefault();
        TTransport transport = new TSocket(serverInfo.getCenterIp(), serverInfo.getCenterPort(), 30000);
        TProtocol protocol = new TBinaryProtocol(transport);
        BrokerCenter.Client client = new BrokerCenter.Client(protocol);
        transport.open();
        String rlt = client.Invoke(input);
        transport.close();
        return rlt;
    }
    public  static <T> T InvokeObj(HashMap<String, String> input,Class<?> cz) throws TException{
        String rlt=Invoke(input);
        T rltObj = (T) JSON.parseObject(rlt, cz);
        return rltObj;
    }
}
