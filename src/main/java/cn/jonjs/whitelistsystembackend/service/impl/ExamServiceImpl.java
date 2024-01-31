package cn.jonjs.whitelistsystembackend.service.impl;

import cn.jonjs.whitelistsystembackend.mapper.ExamMapper;
import cn.jonjs.whitelistsystembackend.pojo.Exam;
import cn.jonjs.whitelistsystembackend.pojo.Question;
import cn.jonjs.whitelistsystembackend.service.ExamService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    ExamMapper examMapper;

    @Override
    public Exam getSingle(Integer id) {
        return examMapper.selectById(id);
    }

    @Override
    public List<Exam> getList() {
        QueryWrapper<Exam> wrapper = new QueryWrapper<Exam>()
                .select("*");
        return examMapper.selectList(wrapper);
    }

    @Override
    public Question getQuestion(Exam singleExam) {
        return new Question(singleExam.getQuestionJson());
    }

    @Override
    public void delete(Integer id) {
        QueryWrapper<Exam> wrapper = new QueryWrapper<Exam>()
                .select("*")
                .eq("id", id);
        examMapper.delete(wrapper);
    }

    @Override
    public void update(Exam exam) {
        UpdateWrapper<Exam> wrapper = new UpdateWrapper<Exam>()
                .set("question_json", exam.getQuestionJson())
                .set("create_member", exam.getCreateMember())
                .eq("id", exam.getId());
        examMapper.update(wrapper);
    }

    @Override
    public void update(Integer id, String json) {
        UpdateWrapper<Exam> wrapper = new UpdateWrapper<Exam>()
                .set("question_json", json)
                .eq("id", id);
        examMapper.update(wrapper);
    }

    @Override
    public void add(String jsonText, String createMember) {
        Date now = new Date(System.currentTimeMillis());
        Exam exam = new Exam();
        exam.setCreateMember(createMember);
        exam.setQuestionJson(jsonText);
        exam.setCreateTime(now);
        examMapper.insert(exam);
    }
}
