package simulate.dto;

import lombok.Data;

@Data
public class GetUserInfoRequestDto {
    private String query;
    private  int age;
}
