package anno.thrift.module;

import lombok.Getter;

import java.util.HashMap;

public class BaseModule {
    /**
     * FormInput信息
     */
    @Getter
    private HashMap<String, String> input;

    /**
     * 是否通过了授权
     */
    private boolean authorized = false;

    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * 前置初始化方法
     * @param input
     * @return
     */
    public  boolean Init(HashMap<String, String> input)
    {
        this.input = input;
        return true;
    }
}
