package anno.thrift.client;

import anno.thrift.rpc.BrokerService;
import anno.thrift.server.ServerInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;

public class ConnectionFactory extends BasePooledObjectFactory<TTransportExt> {
    @Getter
    private String Ip;
    @Getter
    private Integer Port;

    public ConnectionFactory(String ip, Integer port) {
        this.Ip = ip;
        this.Port = port;
    }


    @Override
    public TTransportExt create() throws Exception {
        TTransportExt tTransportExt = new TTransportExt();
        BrokerService.Client client = null;
        TBinaryProtocol protocol = null;
        TSocket transport = null;

        transport = new TSocket(this.Ip, this.Port);
        protocol = new TBinaryProtocol(transport);
        client = new BrokerService.Client(protocol);
        transport.setConnectTimeout(3000);
        transport.setTimeout(ServerInfo.getDefault().getTimeOut());

        tTransportExt.setTransport(transport);
        tTransportExt.setClient(client);
        return tTransportExt;
    }

    @Override
    public PooledObject<TTransportExt> wrap(TTransportExt tTransportExt) {
        return new DefaultPooledObject<>(tTransportExt);
    }

    @Override
    public PooledObject<TTransportExt> makeObject() throws Exception {
        return this.wrap(this.create());
    }

    @Override
    public void destroyObject(PooledObject<TTransportExt> p) throws Exception {
        if (p.getObject().getTransport().isOpen()) {
            p.getObject().getTransport().close();
        }
        super.destroyObject(p);
    }
}
