package anno.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_member")
public class SysMember {
    @TableId
    private long id;
    private String account;
    private String position;
    private String name;
    private short state;
    private Date timespan;
    private Date rdt;
}
