package cn.jonjs.whitelistsystembackend.service;

import cn.jonjs.whitelistsystembackend.pojo.Exam;
import cn.jonjs.whitelistsystembackend.pojo.Question;

import java.util.HashMap;
import java.util.List;

public interface ExamService {

    Exam getSingle(Integer id);

    List<Exam> getList();

    Question getQuestion(Exam singleExam);

    void delete(Integer id);

    void update(Exam exam);

    void update(Integer id, String json);

    void add(String jsonText, String createMemeber);
}
