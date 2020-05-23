package anno.thrift.client;

import anno.thrift.module.Eng;
import anno.thrift.rpc.Micro;
import anno.thrift.server.ServerInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransmitTrace {
    private final static String Namespace = "Anno.Plugs.Trace";
    private final static String Class = "Trace";
    private final static String Method = "Trace";

    /**
     * 设置调用链 TraceId
     *
     * @param input
     * @param micro
     * @return
     */
    public static sys_trace SetTraceId(Map<String, String> input, Micro micro) {
        ServerInfo serverInfo = ServerInfo.getDefault();
        if (!serverInfo.getTraceOnOff()) {
            return null;
        }
        //分布式追踪不记录自己，防止调用链循环调用 +TraceRecord

        if (input.get(Eng.NAMESPACE) == Namespace && input.get(Eng.CLASS) == Class) {
            return null;
        }
        // 调用链唯一标识+ TraceId
        String TraceId = "TraceId";//调用链唯一标识
        String PreTraceId = "PreTraceId";//上一级调用链唯一标识
        String AppName = "AppName";//调用方AnnoService 名称
        String AppNameTarget = "AppNameTarget";//目标AnnoService 名称
        String TTL = "TTL";//分布式调用计数器 默认0
        String GlobalTraceId = "GlobalTraceId";
        if (input.containsKey(TraceId)) {
            input.put(PreTraceId, input.get(TraceId));//当前TraceId 变为上一级 TraceId（PreTraceId）
            input.put(TraceId, UUID.randomUUID().toString()); //生成新的调用链唯一标识
        } else {
            input.put(TraceId, UUID.randomUUID().toString());
            input.put(PreTraceId, "");
        }
        //追踪全局标识
        if (!input.containsKey(GlobalTraceId)) {
            input.put(GlobalTraceId, UUID.randomUUID().toString());
        }

        // App名称 +AppName
        input.put(AppName, serverInfo.getAppName());
        // 目标APP地址
        if (micro != null) {
            String target = "Target";
            input.put(target,micro.ip+":"+micro.port);
            input.put(AppNameTarget,micro.nickname);
        }
           // App +跳转次数
        if (input.containsKey(TTL)) {
          int ttl=  Integer.parseInt(input.get(TTL))+1;
            input.put(TTL,String.valueOf(ttl));
        } else {
            input.put(TTL, "0");
        }

        return TracePool.CreateTrance(input);
    }

    /**
     * 设置调用链 TraceId
     *
     * @param input
     * @return
     */
    public static sys_trace SetTraceId(HashMap<String, String> input) {
       return SetTraceId(input, null);
    }
}
