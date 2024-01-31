package cn.jonjs.whitelistsystembackend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`student`")
public class Student {

    @TableId(type = IdType.INPUT)
    private String code;
    private String name;
    private String gender;
    private String univ;
    private String level;
    private String major;

}
