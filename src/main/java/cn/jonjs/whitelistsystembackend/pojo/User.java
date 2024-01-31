package cn.jonjs.whitelistsystembackend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("`user`")
public class User {
    @TableId(type = IdType.AUTO)
    @NotNull
    private Long id;
    private String name;
    private String password;
    @NotEmpty
    @Pattern(regexp = "^[1-9][0-9]{5,16}$")
    private String qq;
    @NotNull
    private Integer permission;
    @NotEmpty
    @Pattern(regexp = "^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")
    private String email;
    private String code;
    private LocalDateTime createTime;
}

