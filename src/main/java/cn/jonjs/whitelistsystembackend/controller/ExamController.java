package cn.jonjs.whitelistsystembackend.controller;

import cn.jonjs.whitelistsystembackend.config.RconConfig;
import cn.jonjs.whitelistsystembackend.pojo.Exam;
import cn.jonjs.whitelistsystembackend.pojo.Result;
import cn.jonjs.whitelistsystembackend.service.ExamService;
import cn.jonjs.whitelistsystembackend.service.UserService;
import cn.jonjs.whitelistsystembackend.service.WebsiteService;
import cn.jonjs.whitelistsystembackend.service.WhitelistService;
import cn.jonjs.whitelistsystembackend.util.JWTUtil;
import cn.jonjs.whitelistsystembackend.util.JudgeUtil;
import cn.jonjs.whitelistsystembackend.util.WhitelistUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.glavo.rcon.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.jonjs.whitelistsystembackend.util.JWTUtil.parseToken;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService service;
    @Autowired
    private WhitelistService whitelistService;
    @Autowired
    private WebsiteService websiteService;

    @GetMapping("/list")
    public Result<List<Exam>> list() {
        List<Exam> exams = service.getList();
        return Result.success("获取成功", exams);
    }

    @GetMapping("/getByID")
    public Result<Exam> getByID(
            @RequestParam int id
    ) {
        Exam exam = service.getSingle(id);
        return Result.success("获取成功", exam);
    }

    @PostMapping("/submit")
    public Result<?> submit(
            @RequestHeader("Authorization") String token,
            @RequestBody JSONArray array
    ) throws AuthenticationException, IOException {

        Map<String, Object> loginMap = parseToken(token);
        int loginPermission = Integer.parseInt(loginMap.get("permission").toString());
        String loginName = loginMap.get("name").toString();
        if (loginPermission == 1) {
            return Result.error("已在白名单内, 无法再次提交");
        }

        List<Exam> exams = service.getList();
        double score = JudgeUtil.judge(exams, array);
        Map<String, Object> data = new HashMap<>();
        data.put("score", score);
        data.put("username", loginName);
        double passScore = 10000.0;
        if ( ! Objects.equals(websiteService.getConfig("exam_passScore"), "")) {
            passScore = Double.parseDouble(websiteService.getConfig("exam_passScore"));
        }
        if (score >= passScore) {
            whitelistService.addWhitelist(loginName);
            data.put("pass", true);
        } else {
            data.put("pass", false);
        }
        return Result.success("提交成功", data);
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Exam exam) {
        service.update(exam);
        return Result.success("题目信息已更新");
    }

    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam("id") Integer id) {
        service.delete(id);
        return Result.success("题目删除成功");
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Exam exam) {
        service.add(exam.getQuestionJson(), exam.getCreateMember());
        return Result.success("题目新增成功");
    }

}
