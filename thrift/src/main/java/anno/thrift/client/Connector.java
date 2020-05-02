package anno.thrift.client;

import org.apache.thrift.TException;


import java.util.Map;

public class Connector {
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static String Invoke(Map<String, String> input, String host, int port) throws TException {
        Request request = new Request(host, port);
        request.open();
        String rlt = request.Invoke(input);
        request.close();
        return rlt;
    }
}
