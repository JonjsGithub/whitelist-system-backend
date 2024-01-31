package cn.jonjs.whitelistsystembackend.service.impl;

import cn.jonjs.whitelistsystembackend.mapper.UserMapper;
import cn.jonjs.whitelistsystembackend.pojo.User;
import cn.jonjs.whitelistsystembackend.service.UserService;
import cn.jonjs.whitelistsystembackend.util.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByID(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findByName(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("*")
                .eq("name", name);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User findByEmail(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("*")
                .eq("email", email);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User findByQQ(String qq) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("*")
                .eq("qq", qq);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void register(String name, String password, String qq, String email) {
        String passwordMD5 = MD5Util.getMD5Str(password);

        //        userMapper.add(name, passwordMD5, qq, email, 0);

        User user = new User();
        user.setName(name);
        user.setPassword(passwordMD5);
        user.setEmail(email);
        user.setQq(qq);
        user.setPermission(0);
        userMapper.insert(user);
    }

    @Override
    public void updateInfo(User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .set("qq", user.getQq())
                .set("email", user.getEmail())
                .set("permission", user.getPermission())
                .eq("id", user.getId());
        userMapper.update(wrapper);
    }

    @Override
    public List<User> listPage(Integer pageNum, Integer pageSize) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("*");
        Page<User> page = new Page<>(pageNum, pageSize);
        return userMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public List<User> list() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("*");
        return userMapper.selectList(wrapper);
    }

    @Override
    public int listCount() {
        return list().size();
    }

    @Override
    public void updatePermission(int id, int permission) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .set("permission", permission)
                .eq("id", id);
        userMapper.update(wrapper);
    }

    @Override
    public void updatePassword(int id, String password) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .set("password", MD5Util.getMD5Str(password))
                .eq("id", id);
        userMapper.update(wrapper);
    }

    @Override
    public void delete(int id) {
        userMapper.deleteById(id);
    }

    @Override
    public String getStudentCode(int id) {
        User user = userMapper.selectById(id);
        return user.getCode();
    }


}
