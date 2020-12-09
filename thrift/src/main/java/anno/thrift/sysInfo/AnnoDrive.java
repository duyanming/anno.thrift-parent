package anno.thrift.sysInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AnnoDrive
{
    @JSONField(name="Name")
    public String Name ;
    @JSONField(name="Total")
    public double Total ;
    @JSONField(name="Free")
    public double Free ;
}
