package anno.thrift.client;

import anno.thrift.module.Eng;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 追踪队列池
 */
public class TracePool {
    private static ConcurrentLinkedQueue<sys_trace> TraceQueue = new ConcurrentLinkedQueue<>();

    public static void EnQueue(sys_trace trace, String result) {
        if (trace != null) {
            trace.setUseTimeMs(System.currentTimeMillis() - trace.getTimespan());
            trace.setRequest(result);
            TraceQueue.add(trace);
        }
    }

    public static sys_trace CreateTrance(Map<String, String> input) {
        sys_trace sysTrace = new sys_trace();
        sysTrace.setTimespan(System.currentTimeMillis());
        sysTrace.setInputDictionary(input);
        return sysTrace;
    }

    /// <summary>
    /// 批量发送调用链到 追踪服务器
    /// </summary>
    public static void TryDequeue() {
        if (TraceQueue.isEmpty()) {
            return;
        }

        List<sys_trace> traces = new ArrayList<>();
        reTryDequeue:
        {
            while (!TraceQueue.isEmpty() && traces.size() < 100) {
                sys_trace trace = TraceQueue.poll();
                trace.setIp(GetValueByKey(trace.getInputDictionary(), "X-Original-For"));
                trace.setTraceId(GetValueByKey(trace.getInputDictionary(), "TraceId"));
                trace.setPreTraceId(GetValueByKey(trace.getInputDictionary(), "PreTraceId"));
                trace.setAppName(GetValueByKey(trace.getInputDictionary(), "AppName"));
                trace.setAppNameTarget(GetValueByKey(trace.getInputDictionary(), "AppNameTarget"));
                trace.setTTL(RequestInt32(trace.getInputDictionary(), "TTL"));
                trace.setTarget(GetValueByKey(trace.getInputDictionary(), "Target"));
                trace.setAskchannel(GetValueByKey(trace.getInputDictionary(), "channel"));
                trace.setAskrouter(GetValueByKey(trace.getInputDictionary(), "router"));
                trace.setAskmethod(GetValueByKey(trace.getInputDictionary(), "method"));
                trace.setRequest(JSON.toJSONString(trace.getInputDictionary()));
                trace.setGlobalTraceId(GetValueByKey(trace.getInputDictionary(), "GlobalTraceId"));
                trace.setUname(GetValueByKey(trace.getInputDictionary(), "uname"));

                trace.setRlt(trace.getResponse() == null ? false : trace.getResponse().indexOf("\"Status\":true") > 0);

                if (trace.getRlt()) {
                    trace.setResponse(null);
                }

                traces.add(trace);
            }
            if (traces.size() <= 0) {
                return;
            }
            HashMap<String, String> inputTrace = new HashMap();
            inputTrace.put(Eng.NAMESPACE, "Anno.Plugs.Trace");
            inputTrace.put(Eng.CLASS, "Trace");
            inputTrace.put(Eng.METHOD, "TraceBatch");
            inputTrace.put("traces", JSON.toJSONString(traces));
            try {
                Connector.Invoke(inputTrace);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!TraceQueue.isEmpty()) {
                traces.clear();
                break reTryDequeue;
            }
        }
    }

    //工具


    /// <summary>
    /// 根据键获取值 Int32
    /// </summary>
    /// <param name="input"></param>
    /// <param name="key"></param>
    /// <returns>Int32 Value</returns>
    private static Integer RequestInt32(Map<String, String> input, String key) {
        if (RequestContainsKey(input, key)) {

            return Integer.parseInt(GetValueByKey(input, key));
        } else {
            return null;
        }
    }

    /// <summary>
    /// 根据Key 获取字符串值
    /// </summary>
    /// <param name="input">键</param>
    /// <param name="key">键</param>
    /// <returns>字符串值</returns>
    private static String GetValueByKey(Map<String, String> input, String key) {
        if (RequestContainsKey(input, key)) {
            return input.get(key);
        } else {
            return null;
        }
    }

    /// <summary>
    /// 上下文是否包含 key
    /// </summary>
    /// <param name="input"></param>
    /// <param name="key"></param>
    /// <returns></returns>
    private static Boolean RequestContainsKey(Map<String, String> input, String key) {
        return input.containsKey(key);
    }
}
