package anno.thrift.module;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ActionResult<T> {
    /**
     * 状态
     */
    private boolean status = false;
    /*
     *消息
     */
    private String msg;

    /*
     *字典
     */
    private Map<String, Object> output;

    /**
     * 结果集
     */
    private T outputData;

    /**
     * 构造函数
     */
    public ActionResult() {
        output = new HashMap<String, Object>();
        this.status = true;
    }

    /**
     * 构造函数
     */
    public ActionResult(Boolean status) {
        this();
        this.status = status;
    }

    /**
     * 构造函数
     */
    public ActionResult(Boolean status, T outputData) {
        this(status);
        this.outputData = outputData;
    }

    /**
     * 构造函数
     */
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
