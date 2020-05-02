package anno.thrift.module;

import java.util.HashMap;

public class ActionResultObject extends ActionResult<Object> {
    public ActionResultObject() {
        super();
    }

    /// <summary>
/// 构造函数
/// </summary>
/// <param name="status">状态</param>
    public ActionResultObject(Boolean status) {
        super(status);
    }

    /// <summary>
/// 构造函数
/// </summary>
/// <param name="status">状态</param>
/// <param name="outputData">结果集</param>
    public ActionResultObject(Boolean status, Object outputData) {
        super(status,outputData);
    }

    /// <summary>
/// 构造函数
/// </summary>
/// <param name="status">状态</param>
/// <param name="outputData">结果集</param>
/// <param name="output">字典</param>
    public ActionResultObject(Boolean status, Object outputData, HashMap<String, Object> output) {
        super(status, outputData,output);
    }

    /// <summary>
/// 构造函数
/// </summary>
/// <param name="status">状态</param>
/// <param name="outputData">结果集</param>
/// <param name="output">字典</param>
/// <param name="msg">消息</param>
    public ActionResultObject(Boolean status, Object outputData, HashMap<String, Object> output, String msg) {
        super(status, outputData, output,msg);
    }
}
