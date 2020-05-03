package anno.thrift.client;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ConnectionPool extends GenericObjectPool<TTransportExt> {
    public ConnectionPool(ConnectionFactory factory) {
        super(factory);
    }

    public ConnectionPool(ConnectionFactory factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }

    public ConnectionPool(PooledObjectFactory<TTransportExt> factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
