package cn.jonjs.whitelistsystembackend.controller;

import cn.jonjs.whitelistsystembackend.pojo.Result;
import cn.jonjs.whitelistsystembackend.pojo.Website;
import cn.jonjs.whitelistsystembackend.service.WebsiteService;
import cn.jonjs.whitelistsystembackend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/website")
public class WebsiteController {

    @Autowired
    private WebsiteService websiteService;

    public HashMap<String, String> getAllMap() {
        List<Website> websites = websiteService.getAllConfig();
        HashMap<String, String> map = new HashMap<>();
        for (Website website : websites) {
            map.put(website.getK(), website.getV());
        }
        return map;
    }

    @GetMapping("/getAll")
    public Result<?> getAll() {
        HashMap<String, String> map = getAllMap();
        return Result.success("所有配置项获取成功", map);
    }

    @GetMapping("/getValue")
    public Result<String> getValue(
            @RequestParam("key") String key
    ) {
        return Result.success("配置项获取成功", get(key));
    }

    @PutMapping("/setValue")
    public Result<String> setValue(
            @RequestHeader("Authorization") String token,
            @RequestBody Website website
    ) {
        Map<String, Object> loginMap = JWTUtil.parseToken(token);
        int loginPermission = Integer.parseInt(loginMap.get("permission").toString());
        if (loginPermission < 2) {
                return Result.error("权限不足");
        }
        // 允许操作
        websiteService.set(website);
        return Result.success("网站配置信息已更新");
    }

    public String get(String key) {
        return getAllMap().get(key);
    }

}
