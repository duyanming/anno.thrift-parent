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

import java.text.SimpleDateFormat;
import java.util.Date;


public class AnnoServer {
    ServerInfo serverInfo=ServerInfo.getDefault();
    protected final Logger logger = LoggerFactory.getLogger(AnnoServer.class);
    private TBinaryProtocol.Factory protocolFactory;
    private TTransportFactory transportFactory;
    public void init() {
        protocolFactory = new TBinaryProtocol.Factory();
        transportFactory = new TTransportFactory();
    }
    public void start(int port) {
        BrokerService.Processor  processor = new BrokerService.Processor<BrokerService.Iface>(new BrokerServiceImp());
        init();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        try {
            TServerTransport transport = new TServerSocket(port);
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(transport);
            tArgs.processor(processor);
            tArgs.protocolFactory(protocolFactory);
            tArgs.transportFactory(transportFactory);
            tArgs.minWorkerThreads(serverInfo.getMinThreads());
            tArgs.maxWorkerThreads(serverInfo.getMaxThreads());
            TServer server = new TThreadPoolServer(tArgs);
            System.out.println(df.format(new Date())+"--------Anno Service started successfully, Port="+port);
            server.serve();
        } catch (Exception e) {
            logger.error(df.format(new Date())+"--------Anno Service startup failure", e);
        }
    }
}
