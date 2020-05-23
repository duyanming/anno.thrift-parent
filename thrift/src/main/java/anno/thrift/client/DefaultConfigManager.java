package anno.thrift.client;

import anno.thrift.server.ServerInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.thrift.TException;

import java.util.Timer;

/**
 * RPC 客户端配置
 */
public class DefaultConfigManager {
    private static Timer timer = new Timer();
    @Getter
    private static ConnectionPoolConfiguration DefaultConnectionPoolConfiguration;

    public static void SetDefaultConfiguration(String appName, String centerAddress) throws TException {
        SetDefaultConfiguration(appName, centerAddress, 6660);
    }

    public static void SetDefaultConfiguration(String appName, String centerAddress, Integer port,Boolean traceOnOff) throws TException {
        ServerInfo serverInfo = ServerInfo.getDefault();
        serverInfo.setAppName(appName);
        serverInfo.setLocalAddress(centerAddress);
        serverInfo.setPort(port);
        serverInfo.setTraceOnOff(traceOnOff);
        Connector.UpdateCache("");
        // 启动0秒后，每隔2秒执行1次
        timer.schedule(new MyTimerTask(() -> {
            try {
                Connector.UpdateCache("");
            } catch (TException e) {
                e.printStackTrace();
            }
            TracePool.TryDequeue();
        }), 5000, 5000);
    }
    public static void SetDefaultConfiguration(String appName, String centerAddress, Integer port) throws TException {
        SetDefaultConfiguration(appName,centerAddress, port,true);
    }
    /**
     * 设置连接池信息
     *
     * @param maxActive
     * @param minIdle
     * @param maxIdle
     */
    public static void SetDefaultConnectionPool(int maxActive, int minIdle, int maxIdle) {
        if (DefaultConnectionPoolConfiguration == null) {
            DefaultConnectionPoolConfiguration = new ConnectionPoolConfiguration();
        }
        DefaultConnectionPoolConfiguration.setMaxActive(maxActive);
        DefaultConnectionPoolConfiguration.setMinIdle(minIdle);
        DefaultConnectionPoolConfiguration.setMaxIdle(maxIdle);
    }
}
