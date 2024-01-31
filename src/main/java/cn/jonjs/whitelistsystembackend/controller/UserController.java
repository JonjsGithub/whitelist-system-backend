package cn.jonjs.whitelistsystembackend.controller;

import cn.jonjs.whitelistsystembackend.pojo.Result;
import cn.jonjs.whitelistsystembackend.pojo.User;
import cn.jonjs.whitelistsystembackend.service.StudentService;
import cn.jonjs.whitelistsystembackend.service.UserService;
import cn.jonjs.whitelistsystembackend.util.*;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private FindPwdUtil findPwdUtil;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/vcodeStatus")
    public Result<?> vcodeStatus(
            @RequestParam("vcode") String vcode
    ) {
        if (RedisUtils.get(stringRedisTemplate, vcode) == null) {
            return Result.error("验证码已失效, 无法重置密码");
        } else {
            return Result.success("验证码有效");
        }
    }

    @PostMapping("/resetPassword")
    public Result<?> resultPassword(
            @RequestParam("vcode") String vcode,
            @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,32}$")
            @RequestParam("newPassword") String newPassword,
            @RequestParam("newPasswordRepeated") String newPasswordRepeated
    ) {
        if (RedisUtils.get(stringRedisTemplate, vcode) == null) {
            return Result.error("验证码已失效, 无法重置密码");
        }
        if ( ! newPassword.equals(newPasswordRepeated)) {
            return Result.error("两次输入的密码不一致");
        }
        Map<String, Object> map = findPwdUtil.parseVCode(vcode);
        String name = (String) map.get("name");
        User user = service.findByName(name);
        if (user == null) {
            return Result.error("未查询到用户");
        }
        int id = Math.toIntExact(user.getId());
        service.updatePassword(id, newPassword);
        return Result.success("密码重置成功");
    }

    @PostMapping("/findPassword")
    public Result<?> findPassword(
            @RequestParam("name") String name,
            @RequestParam("email") String email
    ) {
        User user = service.findByName(name);
        if (user == null) {
            return Result.error("用户不存在");
        }
        String emailRight = user.getEmail();
        if ( ! email.equals(emailRight)) {
            return Result.error("邮箱不匹配");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        String vcode = findPwdUtil.generateVCode(map);
        RedisUtils.set(stringRedisTemplate, vcode, vcode, findPwdUtil.getExpireMin(),
                TimeUnit.MINUTES);
        boolean isSuccess = mailUtil.sendVCodeMail(name, email, vcode);
        if(isSuccess) {
            return Result.success("邮件已发送, 请在规定时间内点击链接重置密码!");
        } else {
            return Result.error("邮件发送失败, 请联系管理员!");
        }
    }

    @PostMapping("/changePassword")
    public Result<?> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestParam("oldPassword") String oldPassword,
            @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,32}$")
            @RequestParam("newPassword") String newPassword,
            @RequestParam("newPasswordRepeated") String newPasswordRepeated
    ) {
        Map<?, ?> map = JWTUtil.parseToken(token);
        int loginId = Integer.parseInt(map.get("id").toString());
        String loginPasswordMD5 = map.get("password").toString();
        String oldPasswordMD5 = MD5Util.getMD5Str(oldPassword);
        if ( ! newPassword.equals(newPasswordRepeated)) {
            return Result.error("两次输入的新密码不相同");
        }
        if ( ! loginPasswordMD5.equals(oldPasswordMD5)) {
            return Result.error("原密码错误");
        }
        service.updatePassword(loginId, newPassword);
        logout(token);
        return Result.success("密码修改成功, 请重新登录");
    }

    @DeleteMapping("/delete")
    public Result<?> delete(@RequestHeader("Authorization") String token,
                            @RequestParam("id") Integer id) {
        Map<String, Object> loginMap = JWTUtil.parseToken(token);
        int loginPermission = Integer.parseInt(loginMap.get("permission").toString());
        int loginId = Integer.parseInt(loginMap.get("id").toString());
        if (loginPermission < 2 && loginId != id) {
            return Result.error("无权限删除该用户");
        }
        service.delete(id);
        return Result.success("该用户删除成功");
    }

    @GetMapping("/listPage")
    public Result<?> listPage(
            @RequestHeader("Authorization") String token,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ) {
        Map<String, Object> loginMap = JWTUtil.parseToken(token);
        int loginPermission = Integer.parseInt(loginMap.get("permission").toString());
        if (loginPermission < 2) {
            return Result.error("获取失败");
        }
        List<User> userList = service.listPage(pageNum, pageSize);
        return Result.success("获取成功", userList);
    }

    @GetMapping("/list")
    public Result<?> list(
            @RequestHeader("Authorization") String token
    ) {
        Map<String, Object> loginMap = JWTUtil.parseToken(token);
        int loginPermission = Integer.parseInt(loginMap.get("permission").toString());
        if (loginPermission < 2) {
            return Result.error("获取失败");
        }
        List<User> userList = service.list();
        return Result.success("获取成功", userList);
    }

    @GetMapping("/listCount")
    public Result<?> listCount() {
        int count = service.listCount();
        return Result.success("获取成功", count);
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader("Authorization") String token,
                                 @RequestParam(
                                         value = "id",
                                         required = false,
                                         defaultValue = "0"
                                 ) int id) {
        if (id == 0) {
            Map<String, Object> map = JWTUtil.parseToken(token);
            String name = (String) map.get("name");
            User user = service.findByName(name);
            System.out.println(user);
            return Result.success("操作成功", user);
        } else {
            User user = service.findByID(id);
            System.out.println("ID"+id);
            System.out.println(user);
            return Result.success("操作成功", user);
        }
    }

    @PutMapping("/updateInfo")
    public Result<?> updateInfo(@RequestHeader("Authorization") String token,
                                @RequestBody @Validated User user) {
        Map<String, Object> loginMap = JWTUtil.parseToken(token);
        long loginId = Integer.parseInt(loginMap.get("id").toString());
        int loginPermission = Integer.parseInt(loginMap.get("permission").toString());
        long userId = user.getId();
        int userPermission = user.getPermission();
        if (loginPermission < 2) {
            if (userId != loginId || userPermission != loginPermission) {
                return Result.error("权限不足");
            }
        }
        // 允许操作
        service.updateInfo(user);
        return Result.success("用户信息已更新");
    }

    @PostMapping("/login")
    public Result<?> login(
            @RequestParam("name")
            @Pattern(regexp = "^[a-zA-Z0-9_]{3,24}$")
            String name,
            @RequestParam("password")
            @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,32}$")
            String password
    ) {
        User user = null;
        user = service.findByName(name);
        if (user == null) {
            return Result.error("用户名不存在");
        }

        String passwordMD5 = MD5Util.getMD5Str(password);
        String passwordMD5Right = user.getPassword();

        if ( ! passwordMD5.equals(passwordMD5Right)) {
            return Result.error("密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("qq", user.getQq());
        claims.put("password", user.getPassword());
        claims.put("email", user.getEmail());
        claims.put("code", user.getCode());
        claims.put("permission", user.getPermission());
        String token = jwtUtil.generateToken(claims);
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("token", token);
        returnData.put("expire", JWTUtil.expires);
        RedisUtils.set(stringRedisTemplate, token, token, jwtUtil.getExpireMin(), TimeUnit.MINUTES);
        return Result.success("登录成功", returnData);

    }

    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        if (token == null) {
            return Result.error("用户未登录");
        }
        RedisUtils.delete(stringRedisTemplate, token);
        return Result.success("账号登出成功");
    }

    @GetMapping("/getStudentCode")
    public Result<String> getStudentCode(
            @RequestHeader("Authorization") String token,
            @RequestParam("id") int id
    ) {
        Map<String, Object> loginMap = JWTUtil.parseToken(token);
        long loginId = Integer.parseInt(loginMap.get("id").toString());
        int loginPermission = Integer.parseInt(loginMap.get("permission").toString());
        if (loginPermission < 2) {
            if ((long) id != loginId) {
                return Result.error("权限不足");
            }
        }
        // 允许操作
        service.getStudentCode(id);
        return Result.success("学信网在线验证码获取成功");
    }

    @PostMapping("/register")
    public Result<String> register(
            @RequestParam("name")
            @Pattern(regexp = "^[a-zA-Z0-9_]{3,24}$")
            String name,
            @RequestParam("password")
            @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,32}$")
            String password,
            @RequestParam("qq")
            @Pattern(regexp = "^[1-9][0-9]{5,16}$")
            String qq,
            @RequestParam("email")
            @Pattern(regexp = "^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)" +
                    "+[a-z]{2,}$")
            String email
    ) {

        // 用户名是否被占用
        User user = null;
        user = service.findByName(name);
        if (user != null) {
            return Result.error("用户名已被占用");
        }
        // QQ号是否被占用
        user = service.findByQQ(name);
        if (user != null) {
            return Result.error("QQ号码已被占用");
        }
        // 邮箱地址是否被占用
        user = service.findByEmail(name);
        if (user != null) {
            return Result.error("电子邮箱地址已被占用");
        }

        // 注册
        service.register(name, password, qq, email);
        return Result.success("注册成功");

    }


}
