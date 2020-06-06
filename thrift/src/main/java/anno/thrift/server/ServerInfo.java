package anno.thrift.server;

import anno.thrift.sysInfo.UseSysInfoWatch;
import lombok.Data;

@Data
public class ServerInfo {
    private static ServerInfo Default;
    private Boolean TraceOnOff=true;
    private int TimeOut = 3000;
    private String FuncName;
    private String LocalAddress = "127.0.0.1";
    private int Port = 6659;
    private int Weight = 1;
    private String AppName = "AnnoServer";
    private int MinThreads = 4;
    private int MaxThreads = 500;

    private ServerInfo() {

    }

    public static ServerInfo getDefault() {
        if (Default == null) {
            Default = new ServerInfo();
            UseSysInfoWatch.SysInit();
        }
        return Default;
    }
}
