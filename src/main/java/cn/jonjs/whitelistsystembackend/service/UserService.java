package cn.jonjs.whitelistsystembackend.service;

import cn.jonjs.whitelistsystembackend.pojo.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface UserService {

    User findByID(int id);

    User findByName(String name);

    User findByEmail(String email);

    User findByQQ(String qq);

    void register(String name, String password, String qq, String email);

    void updateInfo(User user);

    List<User> listPage(Integer pageNum, Integer pageSize);

    List<User> list();

    int listCount();

    void updatePermission(int id, int permission);

    void updatePassword(int id, String password);

    void delete(int id);

    String getStudentCode(int id);
}
