package anno.thrift.client;

import anno.thrift.module.Eng;
import anno.thrift.rpc.BrokerCenter;
import anno.thrift.rpc.BrokerService;
import anno.thrift.rpc.Micro;
import anno.thrift.server.ServerInfo;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class Connector {
    private static volatile Map<String, ConnectionPool> pools = new HashMap<>();

    private static volatile List<MicroCache> microCaches = new ArrayList<>();

    private String host;
    private int port;

    public static String Invoke(Map<String, String> input) throws Exception {
        MicroCache micro = Single(input.get(Eng.NAMESPACE));
        if (micro == null) {
            return FailMessage("The target service was not found");
        }
        ConnectionPool pool = pools.get(micro.getId());
        String rlt = "";
        TTransportExt tTransportExt = null;
        if (pool != null) {
            try {
                tTransportExt = pool.borrowObject();
                rlt = tTransportExt.getClient().broker(input);
            } catch (Exception ex) {
                rlt = FailMessage(ex.getMessage());
            } finally {
                if (tTransportExt != null) {
                    pool.returnObject(tTransportExt);
                }
            }
        }
        return rlt;
    }

    private static TSocket transport = new TSocket(ServerInfo.getDefault().getLocalAddress(), ServerInfo.getDefault().getPort());
    private static TBinaryProtocol protocol = new TBinaryProtocol(transport);
    private static BrokerCenter.Client client = new BrokerCenter.Client(protocol);

    /**
     * 快速失败消息
     *
     * @param message
     * @return
     */
    public static String FailMessage(String message) {
        return FailMessage(message, false);
    }

    /**
     * 快速失败消息
     *
     * @param message
     * @param status
     * @return
     */
    public static String FailMessage(String message, Boolean status) {
        return "{\"Msg\":\"" + message + "\",\"Status\":" + status.toString().toLowerCase() +
                ",\"Output\":null,\"OutputData\":null}";
    }

    /**
     * 路由管道
     *
     * @param channel
     * @return
     */
    private static MicroCache Single(String channel) {
        List<MicroCache> ms = microCaches.stream()
                .filter(microCache -> microCache.getTags().contains(channel))
                .collect(Collectors.toList());
        Random rd = new Random(UUID.randomUUID().hashCode());
        if (ms.size() > 0) {
            return ms.get(rd.nextInt(ms.size()));
        }

        return null;
    }

    /**
     * 更新服务缓存
     *
     * @param channel
     */
    public static void UpdateCache(String channel) throws TException {
        try {
            if (!transport.isOpen()) {
                transport.open();
            }
            List<Micro> micros = client.GetMicro(channel);
            if (micros != null) {
                /**
                 *销毁注册中心没有的服务
                 */
                int size = microCaches.size();
                for (int i = 0; i < size; i++) {
                    MicroCache microCache = microCaches.get(i);
                    String ip = microCache.getMi().ip;
                    Integer port = microCache.getMi().port;
                    Boolean exist = false;
                    for (Micro m : micros) {
                        if (m.getIp().equals(ip) && m.getPort() == port) {
                            exist = true;
                        }
                    }
                    if (exist) {

                        ConnectionPool connectionPool = pools.remove(microCache.getId());
                        connectionPool.close();
                        microCaches.remove(microCache);
                    }
                }

                /**
                 *创建连接池
                 */
                ServerInfo serverInfo = ServerInfo.getDefault();
                micros.forEach(m ->
                {
                    MicroCache microCache = new MicroCache();
                    microCache.setLasTime(new Date());
                    microCache.setMi(m);
                    List<String> tags = new ArrayList<>();
                    String[] funcs = m.getName().split(",");
                    for (String tag : funcs) {
                        if (tag.length() > 7) {
                            tags.add(tag.substring(0, tag.length() - 7));
                        }
                    }
                    microCache.setTags(tags);
                    microCaches.add(microCache);
                    if (!pools.containsKey(microCache.getId())) {
                        ConnectionFactory orderFactory = new ConnectionFactory(m.ip, m.port);
                        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

                        config.setMaxTotal(DefaultConfigManager.getDefaultConnectionPoolConfiguration().getMaxActive());
                        //设置获取连接超时时间
                        config.setMaxWaitMillis(1000);
                        config.setMaxIdle(DefaultConfigManager.getDefaultConnectionPoolConfiguration().getMaxIdle());
                        config.setMinIdle(DefaultConfigManager.getDefaultConnectionPoolConfiguration().getMinIdle());

                        ConnectionPool connectionPool = new ConnectionPool(orderFactory, config);
                        pools.put(microCache.getId(), connectionPool);
                    }
                });
            }
        } catch (Exception ex) {
            transport = new TSocket(ServerInfo.getDefault().getLocalAddress(), ServerInfo.getDefault().getPort());
            protocol = new TBinaryProtocol(transport);
            client = new BrokerCenter.Client(protocol);
        }
    }
}
