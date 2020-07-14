package anno.thrift.storage;

import lombok.Data;

import java.util.List;

@Data
public class DataValue {
    private String name;
    private String desc;
    private List<ParametersValue> parameters;
}
