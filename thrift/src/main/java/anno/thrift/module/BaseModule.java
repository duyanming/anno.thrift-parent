package anno.thrift.module;

import java.util.HashMap;

public class BaseModule {
    /// <summary>
    /// FormInput信息
    /// </summary>
    private HashMap<String, String> _input;

    public HashMap<String, String> get_input() {
        return _input;
    }

    /// <summary>
    /// 是否通过了授权
    /// </summary>
    private boolean authorized = false;

    public boolean isAuthorized() {
        return authorized;
    }
    /// <summary>
    /// 前置初始化方法
    /// </summary>
    /// <param name="input">表单数据</param>
    /// <returns>是否成功</returns>
    public  boolean Init(HashMap<String, String> input)
    {
        _input = input;
        return true;
    }
}
