import anno.configuration.AnnoConfig;
import anno.thrift.server.AnnoServer;
import anno.thrift.server.ServerInfo;

import java.io.IOException;

public class AnnoServerBoot {
    public static void main(String[] args) throws InterruptedException, IOException {
        AnnoConfig config=new AnnoConfig("application.yml");
         /*
        配置服务基础信息 从配置文件读取
        为了测试目前写死
         */
        ServerInfo serverInfo = ServerInfo.getDefault();
        serverInfo.setAppName(config.getAppName());
        serverInfo.setPort(config.getPort());
        serverInfo.setWeight(config.getWeight());
        serverInfo.setTimeOut(config.getTimeOut());
        serverInfo.setFuncName(config.getFuncName());

        new Thread(() -> {
            new AnnoServer().start(ServerInfo.getDefault().getPort());
        }).start();

        anno.thrift.server.Register.ToCenter(config.getCenterIp(), config.getCenterPort(), 60);
        while (true) {
            Thread.sleep(3000);
        }
    }
}
