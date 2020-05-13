package anno.thrift.storage;

import lombok.Data;

@Data
public class AnnoDataResult<T> {
    /**
     *  状态
     */
    private Boolean status ;
    /**
     * 消息
     */
    public String msg ;
    /**
     *  数据
     */
    public T data ;
}
