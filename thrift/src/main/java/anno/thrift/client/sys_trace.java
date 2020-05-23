package anno.thrift.client;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@Accessors(chain = true)
public class sys_trace  implements Serializable {
    private long ID;
    /**
     * 调用链全局唯一标识
     */
    private String GlobalTraceId;
    /**
     * 调用链唯一标识
     */
    private String TraceId;
    /**
     * 上级调用链唯一标识
     */
    private String PreTraceId;
    /**
     * 调用方App名称
     */
    private String AppName;
    /**
     * 目标App名称
     */
    private String AppNameTarget;
    /**
     * 跳转次数
     */
    private Integer TTL;
    /**
     * 请求参数
     */
    private String Request;
    /**
     * 响应参数
     */
    private String Response;
    /**
     * 处理结果
     */
    private Boolean Rlt;
    /**
     * 操作人IP
     */
    private String Ip;
    /**
     * 目标地址
     */
    private String Target;
    /**
     * 操作人ID
     */
    private Long UserId;
    /**
     * 操作人名称
     */
    private String Uname;
    /**
     * 记录时间
     */
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date Timespan;
    /// <summary>
    /// 请求管道
    /// </summary>
    private String Askchannel;
    /**
     * 请求路由
     */
    private String Askrouter;
    /// <summary>
    /// 业务方法
    /// </summary>
    private String Askmethod;

    @JSONField(serialize=false)
    private Map<String, String> InputDictionary;

    /**
     * 耗时单位毫秒
     */
    private double UseTimeMs;
}
