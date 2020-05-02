package anno.thrift.server;

import lombok.Data;

@Data
public  class ServerInfo {
    private static ServerInfo Default;
    private int TimeOut=3000;
    private String FuncName;
    private String LocalAddress="127.0.0.1";
    private int Port=6659;
    private  int Weight=1;
    private String AppName="AnnoServer";

    private ServerInfo(){

    }
    public static ServerInfo getDefault() {
        if(Default==null){
            Default=new ServerInfo();
        }
        return Default;
    }
}
