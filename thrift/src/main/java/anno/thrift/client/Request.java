package anno.thrift.client;

import anno.thrift.rpc.BrokerService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import java.util.Map;

public class Request{
    private BrokerService.Client client;
    private TBinaryProtocol protocol;
    private TSocket transport;
    public  Request(String host,int port) {
        transport = new TSocket(host, port);
        protocol = new TBinaryProtocol(transport);
        client = new BrokerService.Client(protocol);
        transport.setConnectTimeout(3000);
        transport.setTimeout(20000);
    }

    public String Invoke(Map<String,String> input) throws TException {
        return client.broker(input);
    }

    public void open() throws TTransportException {
        transport.open();
    }

    public void close() {
        transport.close();
    }
}
