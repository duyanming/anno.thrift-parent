package anno.thrift.module;

import java.util.HashMap;

public class ActionResultObject extends ActionResult<Object> {
    public ActionResultObject() {
        super();
    }

    /**
     * 构造函数
     * @param status
     */
    public ActionResultObject(Boolean status) {
        super(status);
    }

    /**
     * 构造函数
     * @param status
     * @param outputData
     */
    public ActionResultObject(Boolean status, Object outputData) {
        super(status, outputData);
    }

    /**
     * 构造函数
     * @param status
     * @param outputData
     * @param output
     */
    public ActionResultObject(Boolean status, Object outputData, HashMap<String, Object> output) {
        super(status, outputData, output);
    }

    /**
     * 构造函数
     * @param status
     * @param outputData
     * @param output
     * @param msg
     */
    public ActionResultObject(Boolean status, Object outputData, HashMap<String, Object> output, String msg) {
        super(status, outputData, output, msg);
    }
}
