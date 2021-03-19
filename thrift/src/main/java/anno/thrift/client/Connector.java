package anno.thrift.client;

import anno.thrift.module.ActionResult;
import anno.thrift.module.Eng;
import anno.thrift.rpc.BrokerCenter;
import anno.thrift.rpc.BrokerService;
import anno.thrift.rpc.Micro;
import anno.thrift.server.ServerInfo;
import com.alibaba.fastjson.JSON;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Connector {
  private static volatile ConcurrentHashMap<String, ConnectionPool> pools =
      new ConcurrentHashMap<>();
  private static volatile List<MicroCache> microCaches = new ArrayList<>();
  private static final int MAX_TIMES = 100;

  /**
   * 调用业务
   *
   * @param input
   * @return
   * @throws Exception
   */
  public static String Invoke(Map<String, String> input) throws Exception {
    MicroCache micro = Single(input.get(Eng.NAMESPACE));
    String rlt = Invoke(input, micro);
    return rlt;
  }

  /**
   * 根据指定的Micro 信息调用指定业务的服务
   *
   * @param input
   * @param micro
   * @return
   * @throws Exception
   */
  public static String Invoke(Map<String, String> input, MicroCache micro) throws Exception {
    if (!input.containsKey(Eng.NAMESPACE)
        || !input.containsKey(Eng.CLASS)
        || !input.containsKey(Eng.METHOD)) {
      return FailMessage(
          "Missing keywords（" + Eng.NAMESPACE + "、" + Eng.CLASS + "、" + Eng.METHOD + "）");
    }
    if (micro == null) {
      return FailMessage("The target service was not found");
    }
    ConnectionPool pool = pools.get(micro.getId());
    String rlt = "";
    TTransportExt tTransportExt = null;
    if (pool != null) {
      int time = 0;
      while (time < MAX_TIMES) {
        boolean isException = false;
        sys_trace trace = TransmitTrace.SetTraceId(input, micro.getMi());
        try {
          tTransportExt = pool.borrowObject(micro.getMi().timeout);
          rlt = tTransportExt.getClient().broker(input);
          //跳出循环结束重试
          time =MAX_TIMES+1;
        } catch (Exception ex) {
          isException = true;
          pool.invalidateObject(tTransportExt);
            time++;
            if(time >= MAX_TIMES){
              throw  ex;
            }
          rlt = FailMessage(ex.getMessage());
        } finally {
          if (isException == false && tTransportExt != null) {
            pool.returnObject(tTransportExt);
          }
          TracePool.EnQueue(trace, rlt);
        }
      }
    } else {
      rlt = FailMessage("No object pool was found");
    }
    return rlt;
  }

  /**
   * 调用业务 返回对象
   *
   * @param input
   * @param <T>
   * @return
   * @throws Exception
   */
  public static <T> ActionResult<T> InvokeObj(Map<String, String> input) throws Exception {
    String rlt = Invoke(input);
    return JSON.parseObject(rlt, new ActionResult<T>().getClass());
  }

  /**
   * 根据指定的Micro 信息调用指定业务的服务
   *
   * @param input
   * @param micro
   * @param <T>
   * @return
   * @throws Exception
   */
  public static <T> ActionResult<T> InvokeObj(Map<String, String> input, MicroCache micro)
      throws Exception {
    String rlt = Invoke(input, micro);
    return JSON.parseObject(rlt, new ActionResult<T>().getClass());
  }

  private static TSocket transport =
      new TSocket(ServerInfo.getDefault().getLocalAddress(), ServerInfo.getDefault().getPort());
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
    return "{\"msg\":\""
        + message
        + "\",\"status\":"
        + status.toString().toLowerCase()
        + ",\"output\":null,\"outputData\":null}";
  }

  /**
   * 路由管道
   *
   * @param channel
   * @return
   */
  private static MicroCache Single(String channel) {
    List<MicroCache> ms =
        microCaches.stream()
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
        /** 销毁注册中心没有的服务 */
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
          if (!exist) {

            ConnectionPool connectionPool = pools.remove(microCache.getId());
            connectionPool.close();
            microCaches.remove(microCache);
          }
        }

        /** 创建连接池 */
        ServerInfo serverInfo = ServerInfo.getDefault();
        List<MicroCache> _microCaches = new ArrayList<>();
        micros.forEach(
            m -> {
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
              _microCaches.add(microCache);

              if (!pools.containsKey(microCache.getId())) {
                GenericObjectPoolConfig config = new GenericObjectPoolConfig();

                config.setMaxTotal(
                        DefaultConfigManager.getDefaultConnectionPoolConfiguration().getMaxActive());
                // 设置获取连接超时时间
                config.setMaxWaitMillis(1000);
                config.setMaxIdle(
                        DefaultConfigManager.getDefaultConnectionPoolConfiguration().getMaxIdle());
                config.setMinIdle(
                        DefaultConfigManager.getDefaultConnectionPoolConfiguration().getMinIdle());


                ConnectionFactory orderFactory = new ConnectionFactory(m.ip, m.port,m.timeout);
                ConnectionPool connectionPool = new ConnectionPool(orderFactory, config);
                pools.put(microCache.getId(), connectionPool);
              }else {
                ConnectionPool pool = pools.get(microCache.getId());
                ((ConnectionFactory)pool.getFactory()).setTimeOut(microCache.getMi().timeout);
              }
            });
        microCaches=_microCaches;
      }
    } catch (Exception ex) {
      transport =
          new TSocket(ServerInfo.getDefault().getLocalAddress(), ServerInfo.getDefault().getPort());
      protocol = new TBinaryProtocol(transport);
      client = new BrokerCenter.Client(protocol);
    }
  }
}
