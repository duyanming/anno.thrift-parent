package anno.thrift.server;

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
    public int getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(int timeOut) {
        TimeOut = timeOut;
    }

    public String getFuncName() {
        return FuncName;
    }

    public void setFuncName(String funcName) {
        FuncName = funcName;
    }

    public String getLocalAddress() {
        return LocalAddress;
    }

    public void setLocalAddress(String localAddress) {
        LocalAddress = localAddress;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }
}
