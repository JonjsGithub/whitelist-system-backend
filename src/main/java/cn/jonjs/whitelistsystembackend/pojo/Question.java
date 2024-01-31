package cn.jonjs.whitelistsystembackend.pojo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class Question {

    private String title;
    private String type;
    private String right;
    private double score;

    public Question(String jsonText) {
        JSONObject obj = JSONObject.parseObject(jsonText);
        // 题目文本
        String title = (String) obj.get("title");
        // 题目类型
        String type = (String) obj.get("type");
        // 正确答案
        String right = (String) obj.get("right");
        // 分数
        BigDecimal scoreBD = (BigDecimal) obj.get("score");
        double score = scoreBD.doubleValue();
        // 选项
        this.title = title;
        this.type = type;
        this.right = right;
        this.score = score;
    }



}
