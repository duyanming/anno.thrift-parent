package anno.thrift.client;

import anno.thrift.rpc.BrokerService;
import lombok.Data;
import org.apache.thrift.transport.TTransport;

/**
 * 数据传输对象扩展
 */
@Data
public class TTransportExt {
    /**
     * Thrift 传输对象
     */
    private TTransport Transport;
    /**
     * BrokerService.Client 客户端对象
     */
    private BrokerService.Client Client;
}
