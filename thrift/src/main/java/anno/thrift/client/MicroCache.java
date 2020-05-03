package anno.thrift.client;

import anno.thrift.rpc.Micro;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class MicroCache {
    /**
     * IP端口唯一标志（IP+Port）
     */
    @Getter
    private String Id;
    /**
     * 功能列表
     */
    @Getter
    @Setter
    private List<String> Tags;
    /**
     * 服务
     */
    @Getter
    private Micro Mi;

    public void setMi(Micro mi) {
        Mi = mi;
        Id=mi.getIp()+":"+mi.getPort();
    }
    @Setter
    @Getter
    private Date LasTime;
}
