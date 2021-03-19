package anno.configuration;

import anno.thrift.module.EnginStrategy;
import anno.thrift.module.Engine;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Data
@Configuration
@PropertySource(ignoreResourceNotFound = true,
        value = {"classpath:/application.yml"},
        encoding = "utf-8",
        factory = ResourceFactory.class)
public class AnnoConfig {
    private AnnoConfig annoConfig;

    @Value("${anno.appName}")
    private String appName;
    @Value("${anno.port}")
    private int port;
    @Value("${anno.weight}")
    private int weight;
    @Value("${anno.timeOut}")
    private int timeOut;
    @Value("${anno.funcName}")
    private String funcName;
    @Value("${anno.centerIp}")
    private String centerIp;
    @Value("${anno.centerPort}")
    private int centerPort;
    @Value("${anno.reTry}")
    private int reTry;
    @Value("${anno.minThreads}")
    private int minThreads = 4;
    @Value("${anno.maxThreads}")
    private int maxThreads = 500;

    @Bean(name = "AnnoConfig")
    public AnnoConfig getAnnoConfig() {
        annoConfig=new AnnoConfig();
        return annoConfig;
    }
}
