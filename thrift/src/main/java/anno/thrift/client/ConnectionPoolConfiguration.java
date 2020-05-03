package anno.thrift.client;

import lombok.Data;

/**
 * 连接池 调优配置
 */
@Data
public class ConnectionPoolConfiguration {
    /**
     * 最大活动数量 默认500
     */
    private int MaxActive  = 500;
    /**
     * 最小空闲数量(默认个数为 CPU 数量 Runtime.getRuntime().availableProcessors()*2）
     */
    private int MinIdle  = Runtime.getRuntime().availableProcessors()*2;
    /**
     * 最大空闲数量 默认50
     */
    private int MaxIdle  = 50;
}
