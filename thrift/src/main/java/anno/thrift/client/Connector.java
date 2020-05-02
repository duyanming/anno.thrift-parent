package anno.thrift.client;

import lombok.Data;
import org.apache.thrift.TException;


import java.util.Map;
@Data
public class Connector {
    private String host;
    private int port;
    public static String Invoke(Map<String, String> input, String host, int port) throws TException {
        Request request = new Request(host, port);
        request.open();
        String rlt = request.Invoke(input);
        request.close();
        return rlt;
    }
}
