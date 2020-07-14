package anno.thrift.storage;

import lombok.Data;

@Data
public class ParametersValue {
    private String name;
    private int position ;
    public String desc;
    private String parameterType ;
}
