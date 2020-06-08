package anno.thrift.sysInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
@Data
public class ServerStatus {
    @JSONField(name="RunTime")
    public String RunTime;
    @JSONField(name="CurrentTime")
    public Date CurrentTime;
    @JSONField(name="Memory")
    public long Memory;
    @JSONField(name="Cpu")
    public double Cpu;
    @JSONField(name="Tag")
    public String Tag;
    @JSONField(name="MemoryTotal")
    public double MemoryTotal ;
    @JSONField(name="MemoryTotalUse")
    public double MemoryTotalUse;
    @JSONField(name="CpuTotalUse")
    public double CpuTotalUse;
}
