package cn.jonjs.whitelistsystembackend.controller;

import cn.jonjs.whitelistsystembackend.pojo.Result;
import cn.jonjs.whitelistsystembackend.service.UserService;
import cn.jonjs.whitelistsystembackend.service.WebsiteService;
import cn.jonjs.whitelistsystembackend.util.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/old")
public class OldController {

    @Autowired
    private UserService userService;
    @Autowired
    private WebsiteService websiteService;

    @PostMapping("/submit")
    public Result<?> submit(
            @RequestHeader("Authorization") String token
    ) {
        Map<String, Object> map = JWTUtil.parseToken(token);
        String name = (String) map.get("name");
        int id = (int) map.get("id");

        String jsonStr = websiteService.getConfig("player_old");
        JSONArray array = JSON.parseArray(jsonStr);
        if (array.contains(name)) {
            userService.updatePermission(id, 1);
            return Result.success("老玩家白名单获取成功, 请前往个人控制台刷新白名单状态");
        } else {
            return Result.error("您不是老玩家");
        }
    }

}
