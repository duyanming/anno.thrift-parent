package anno.thrift.server;

import anno.thrift.rpc.BrokerCenter;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.*;

public class Register {
    static TTransport transport;
    static BrokerCenter.Client client;

    /**
     *注册服务到注册中心
     *tIp：注册中心地址
     *tPort：注册中心端口
     */
    public static void ToCenter(String tIp, int tPort, int countDown) throws InterruptedException {
        String ipsStr = GetLocalIps();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        try {
            transport = new TSocket(tIp, tPort, 3000);
            TProtocol protocol = new TBinaryProtocol(transport);
            client = new BrokerCenter.Client(protocol);

            if (!transport.isOpen()) {
                transport.open();
            }
            HashMap<String, String> input = new HashMap<String, String>();
            input.put("timeout", String.valueOf(ServerInfo.getDefault().getTimeOut()));
            input.put("name", ServerInfo.getDefault().getFuncName());
            input.put("ip", ipsStr);
            input.put("port", String.valueOf(ServerInfo.getDefault().getPort()));
            input.put("weight", String.valueOf(ServerInfo.getDefault().getWeight()));
            input.put("nickname", ServerInfo.getDefault().getAppName());
            boolean rlt = client.add_broker(input);
            transport.close();
            if (rlt) {
                System.out.println(df.format(new Date()));
                System.out.println("本机【" + ServerInfo.getDefault().getAppName() + "】:");
                for (String ip : ipsStr.split(",")) {
                    System.out.println(ip);
                }
                System.out.println("已注册到：" + tIp + ":" + tPort);
                System.out.println("----------------------------------------------------------------- ");
            }
        } catch (Exception ex) {
            Thread.sleep(1000);
            if (countDown > 0) {
                System.out.println("" + df.format(new Date()) + "注册到" + tIp + ":" + tPort + "失败......剩余重试次数" + countDown);
                System.out.println(ex.getMessage());
                try {
                    if (transport.isOpen()) {
                        transport.close();
                    }
                } catch (Exception exe) {
                    //忽略异常
                }
                --countDown;
                ToCenter(tIp, tPort, countDown);
            } else {
                System.out.println("" + df.format(new Date()) + "未连接到" + tIp + ":" + tPort + "注册失败......");
            }
        } finally {
            if (transport.isOpen()) {
                transport.close();
            }
        }
    }

    /**
     * 获取本机IPv4集合字符串
     * @return
     */
    private static String GetLocalIps() {
        StringBuilder ipsBuilder = new StringBuilder();
        try {
            Enumeration<NetworkInterface> interfaces = null;
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    InetAddress nextElement = addresss.nextElement();
                    if (nextElement != null
                            && nextElement instanceof Inet4Address
                            && !nextElement.isLoopbackAddress()
                            && nextElement.getHostAddress().indexOf(":") == -1) {
                        String hostAddress = nextElement.getHostAddress();
                        if (ipsBuilder.length() > 0) {
                            ipsBuilder.append(",");
                        }
                        ipsBuilder.append(hostAddress);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipsBuilder.toString();
    }
}
