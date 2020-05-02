package anno.thrift.server;

import anno.thrift.imp.BrokerServiceImp;
import anno.thrift.rpc.BrokerService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AnnoServer {
    protected final Logger logger = LoggerFactory.getLogger(AnnoServer.class);
    private int maxThreads=500;
    private TBinaryProtocol.Factory protocolFactory;
    private TTransportFactory transportFactory;
    public void init() {
        protocolFactory = new TBinaryProtocol.Factory();
        transportFactory = new TTransportFactory();
    }
    public void start(int port) {
        BrokerService.Processor  processor = new BrokerService.Processor<BrokerService.Iface>(new BrokerServiceImp());
        init();
        try {
            TServerTransport transport = new TServerSocket(port);
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(transport);
            tArgs.processor(processor);
            tArgs.protocolFactory(protocolFactory);
            tArgs.transportFactory(transportFactory);
            int minThreads = 4;
            tArgs.minWorkerThreads(minThreads);
            tArgs.maxWorkerThreads(maxThreads);
            TServer server = new TThreadPoolServer(tArgs);
            logger.info("Anno服务启动成功, 端口={}", port);
            server.serve();
        } catch (Exception e) {
            logger.error("Anno服务启动失败", e);
        }
    }
}
