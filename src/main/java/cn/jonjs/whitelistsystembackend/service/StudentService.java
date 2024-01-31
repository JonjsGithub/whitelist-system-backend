package cn.jonjs.whitelistsystembackend.service;

import cn.jonjs.whitelistsystembackend.pojo.Student;

public interface StudentService {

    Student findByCode(String code);

    void update(int uid, Student student);

    void save(int uid, Student student);
}
