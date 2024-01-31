package cn.jonjs.whitelistsystembackend.controller;

import cn.jonjs.whitelistsystembackend.pojo.Result;
import cn.jonjs.whitelistsystembackend.pojo.Student;
import cn.jonjs.whitelistsystembackend.service.StudentService;
import cn.jonjs.whitelistsystembackend.util.StudentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/validate") //带储存到数据库
    public Result<?> query(
            @RequestParam("id") int id,
            @RequestParam("code") String code
    ) {
        HashMap<String, String> map = StudentUtil.getXueJiInfo(code);
        if (map != null && map.size() >= 2) {
            Student student = new Student();
            student.setCode(map.get("代码"));
            student.setName(map.get("姓名"));
            student.setGender(map.get("性别"));
            student.setUniv(map.get("院校"));
            student.setLevel(map.get("层次"));
            student.setMajor(map.get("专业"));
            studentService.save(id, student);
            return Result.success("验证成功, 数据已保存", map);
        } else {
            return Result.error("验证失败");
        }
    }

    @GetMapping("/getByCode")
    public Result<?> getByCode(@RequestParam("code") String code) {
        Student student = studentService.findByCode(code);
        if (student.getUniv() != null) {
            return Result.success("获取成功", student);
        } else {
            return Result.error("获取失败");
        }

    }
}
