package cn.jonjs.whitelistsystembackend.service.impl;

import cn.jonjs.whitelistsystembackend.mapper.UserMapper;
import cn.jonjs.whitelistsystembackend.pojo.User;
import cn.jonjs.whitelistsystembackend.service.WhitelistService;
import cn.jonjs.whitelistsystembackend.util.WhitelistUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.glavo.rcon.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WhitelistServiceImpl implements WhitelistService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WhitelistUtil whitelistUtil;

    @Override
    public int queryNowPermission(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("name", name);
        User newUser = userMapper.selectOne(wrapper);
        return newUser.getPermission();
    }

    @Override
    public void addWhitelist(String name) throws AuthenticationException, IOException {
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .set("permission", 1)
                .eq("name", name);
        userMapper.update(wrapper);
        whitelistUtil.addWhitelist(name);
    }

    @Override
    public void removeWhitelist(String name) throws AuthenticationException, IOException {
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .set("permission", 0)
                .eq("name", name);
        userMapper.update(wrapper);
        whitelistUtil.removeWhitelist(name);
    }

    @Override
    public String connectTest() throws AuthenticationException, IOException {
        return whitelistUtil.connectTest();
    }

}
