package cn.jonjs.whitelistsystembackend.service.impl;

import cn.jonjs.whitelistsystembackend.mapper.StudentMapper;
import cn.jonjs.whitelistsystembackend.mapper.UserMapper;
import cn.jonjs.whitelistsystembackend.pojo.Student;
import cn.jonjs.whitelistsystembackend.pojo.User;
import cn.jonjs.whitelistsystembackend.service.StudentService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Student findByCode(String code) {
        return studentMapper.selectById(code);
    }

    @Override
    public void update(int uid, Student student) {
        studentMapper.updateById(student);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .set("code", student.getCode())
                .set("permission", 1)
                .eq("id", uid);
        userMapper.update(wrapper);
    }

    @Override
    public void save(int uid, Student student) {
        studentMapper.insert(student);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .set("code", student.getCode())
                .set("permission", 1)
                .eq("id", uid);
        userMapper.update(wrapper);
    }

}
