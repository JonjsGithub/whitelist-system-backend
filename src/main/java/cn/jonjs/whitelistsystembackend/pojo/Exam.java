package cn.jonjs.whitelistsystembackend.pojo;


import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@TableName("`exam`")
public class Exam implements Serializable {

    @NotNull
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotEmpty
    @TableField(value = "question_json")
    private String questionJson;
    @NotEmpty
    @TableField(value = "create_member")
    private String createMember;
    @TableField(value = "create_time")
    private Date createTime;

}
