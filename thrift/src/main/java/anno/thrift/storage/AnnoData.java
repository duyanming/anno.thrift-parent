package anno.thrift.storage;

import lombok.Data;

@Data
public class AnnoData {
    /**
     * 来自哪个App
     */
    private String App ;
    /**
     *  键(Key)
     */
    private String Id ;

    /**
     * 值
     */
    private String Value ;
}
