package anno.thrift.module;

import java.util.HashMap;
import java.util.Map;

public class ActionResult<T> {
    private boolean status = false;

    /// <summary>
    /// 状态
    /// </summary>
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean _status) {
        status = _status;
    }

    private String msg;

    /// <summary>
    /// 消息
    /// </summary>
    public String getMsg() {
        return msg;
    }

    public void setMsg(String _msg) {
        msg = _msg;
    }

    /// <summary>
    /// 字典
    /// </summary>
    private Map<String, Object> output;

    public Map<String, Object> getOutput() {
        return output;
    }

    public void setOutput(Map<String, Object> _output) {
        output = _output;
    }

    /// <summary>
    /// 结果集
    /// </summary>
    private T outputData;

    public T getOutputData() {
        return outputData;
    }

    public void setOutputData(T _outputdata) {
        outputData = _outputdata;
    }

    /// <summary>
    /// 构造函数
    /// </summary>
    public ActionResult() {
        output = new HashMap<String, Object>();
        this.status = true;
    }

    /// <summary>
    /// 构造函数
    /// </summary>
    /// <param name="status">状态</param>
    public ActionResult(Boolean status) {
        this();
        this.status = status;
    }

    /// <summary>
    /// 构造函数
    /// </summary>
    /// <param name="status">状态</param>
    /// <param name="outputData">结果集</param>
    public ActionResult(Boolean status, T outputData) {
        this(status);
        this.outputData = outputData;
    }

    /// <summary>
    /// 构造函数
    /// </summary>
    /// <param name="status">状态</param>
    /// <param name="outputData">结果集</param>
    /// <param name="output">字典</param>
    public ActionResult(Boolean status, T outputData, Map<String, Object> output) {
        this(status, outputData);
        this.output = output;
    }

    /// <summary>
    /// 构造函数
    /// </summary>
    /// <param name="status">状态</param>
    /// <param name="outputData">结果集</param>
    /// <param name="output">字典</param>
    /// <param name="msg">消息</param>
    public ActionResult(Boolean status, T outputData, Map<String, Object> output, String msg) {
        this(status, outputData, output);
        this.msg = msg;
    }
}
