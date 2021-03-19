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

    @Getter
    @Setter
    private Integer TimeOut;

    public ConnectionFactory(String ip, Integer port,Integer timeOut) {
        this.Ip = ip;
        this.Port = port;
        this.TimeOut=timeOut;
    }


    @Override
    public TTransportExt create() throws Exception {
        TTransportExt tTransportExt = new TTransportExt();
        TSocket transport = new TSocket(this.Ip, this.Port);
        TBinaryProtocol protocol = new TBinaryProtocol(transport);
        BrokerService.Client client = new BrokerService.Client(protocol);
        transport.setTimeout(this.TimeOut);
        if(transport.isOpen()==false){
            transport.open();
        }
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
            p.getObject().getTransport().flush();
            p.getObject().getTransport().close();
        }
        super.destroyObject(p);
    }
}
