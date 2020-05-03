package anno.configuration;

import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Data
public class AnnoConfig {
    private String appName;
    private int port;
    private int weight;
    private int timeOut;
    private String funcName;
    private String centerIp;
    private int centerPort;
    private int reTry;
    private int minThreads = 4;
    private int maxThreads = 500;

    public AnnoConfig(String configFilePath) throws IOException {
        if (configFilePath == null) {
            configFilePath = "application.properties";
        }
        if (configFilePath.endsWith(".yml")) {
            Yaml yaml = new Yaml();
            try {
                InputStream in = AnnoConfig.class.getClassLoader().getResourceAsStream(configFilePath);
                Map<String, Object> map = (Map<String, Object>) yaml.loadAs(in, Map.class).get("anno");
                appName = (String) map.getOrDefault("appName", "annoServer");
                port = (Integer) map.getOrDefault("port", 6659);
                weight = (Integer) map.getOrDefault("weight", 1);
                timeOut = (Integer) map.getOrDefault("timeOut", 20000);
                funcName = (String) map.getOrDefault("funcName", "");
                centerIp = (String) map.getOrDefault("centerIp", "127.0.0.1");
                centerPort = (Integer) map.getOrDefault("centerPort", 6660);
                reTry = (Integer) map.getOrDefault("reTry", 60);
                minThreads = (Integer) map.getOrDefault("minThreads", 4);
                maxThreads = (Integer) map.getOrDefault("maxThreads", 500);
            } catch (Exception ex) {
                throw ex;
            }
        } else {
            Properties properties = new Properties();
            try {
                // 使用ClassLoader加载properties配置文件生成对应的输入流
                InputStream in = AnnoConfig.class.getClassLoader().getResourceAsStream(configFilePath);
                // 使用properties对象加载输入流
                properties.load(in);
                //获取key对应的value值

                appName = properties.getProperty("anno.appName", "annoServer");
                port = Integer.parseInt(properties.getProperty("anno.port", "6659"));
                weight = Integer.parseInt(properties.getProperty("anno.weight", "1"));
                timeOut = Integer.parseInt(properties.getProperty("anno.timeOut", "2000"));
                funcName = properties.getProperty("anno.funcName", "");
                centerIp = properties.getProperty("anno.centerIp", "127.0.0.1");
                centerPort = Integer.parseInt(properties.getProperty("anno.centerPort", "6660"));
                reTry = Integer.parseInt(properties.getProperty("anno.reTry", "60"));

            } catch (Exception ex) {
                throw ex;
            }
        }
    }
}
