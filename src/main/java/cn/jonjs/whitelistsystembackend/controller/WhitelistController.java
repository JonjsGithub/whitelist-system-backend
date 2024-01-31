package cn.jonjs.whitelistsystembackend.controller;

import cn.jonjs.whitelistsystembackend.config.RconConfig;
import cn.jonjs.whitelistsystembackend.pojo.Result;
import cn.jonjs.whitelistsystembackend.service.WhitelistService;
import cn.jonjs.whitelistsystembackend.util.JWTUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/whitelist")
public class WhitelistController {

    @Autowired
    private WhitelistService service;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RconConfig rconConfig;

    @GetMapping("/rconStatus")
    public Result<?> rconStatus() {
        try {
            String res = service.connectTest();
            return Result.success("Rcon服务器连接成功", res);
        } catch (Exception e) {
            return Result.error("Rcon服务器连接失败");
        }
    }

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success();
    }


    @PostMapping("/add")
    public Result<?> addWhitelist(
            @RequestHeader("Authorization") String token,
            @RequestParam("name")
            @Pattern(regexp = "^[a-zA-Z0-9_]{3,24}$")
            String name
    ) {
        Map<String, Object> loginMap = JWTUtil.parseToken(token);
        String loginName = (String) loginMap.get("name");
        int queryPermission = service.queryNowPermission(loginName);
        if (queryPermission == 0) {
            return Result.error("权限不足");
        }
        if (queryPermission == 1 && ! loginName.equals(name)) {
            return Result.error("游戏名不是本人");
        }
        // 允许操作
        try {
            service.addWhitelist(name);
            return Result.success("已添加至白名单");
        } catch (Exception e) {
            return Result.error("Rcon服务器连接失败");
        }
    }

    @PostMapping("/delete")
    public Result<?> deleteWhitelist(
            @RequestHeader("Authorization") String token,
            @RequestParam("name")
            @Pattern(regexp = "^[a-zA-Z0-9_]{3,24}$")
            String name
    ) {
        Map<String, Object> loginMap = JWTUtil.parseToken(token);
        String loginName = (String) loginMap.get("name");
        int queryPermission = service.queryNowPermission(loginName);
        if (queryPermission < 2) {
            return Result.error("权限不足");
        }
        try {
            service.removeWhitelist(name);
            return Result.success("已从白名单中移除");
        } catch (Exception e) {
            return Result.error("Rcon服务器连接失败");
        }
    }

}
