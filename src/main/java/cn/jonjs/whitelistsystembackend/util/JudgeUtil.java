package cn.jonjs.whitelistsystembackend.util;

import cn.jonjs.whitelistsystembackend.pojo.Exam;
import cn.jonjs.whitelistsystembackend.pojo.Question;
import com.alibaba.fastjson2.JSONArray;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class JudgeUtil {

    public static double judge(List<Exam> exams, JSONArray userAnswers) {
        double score = 0.0D;
        if (exams.isEmpty()) {
            return score;
        }
        if (userAnswers.isEmpty()) {
            return score;
        }
        for (int i=0; i<exams.size(); i++) {
            String json = exams.get(i).getQuestionJson();
            Question question = new Question(json);
            String questionRight = question.getRight();
            double questionScore = question.getScore();
            String userAnswer;
            userAnswer = userAnswers.getString(i);
            if (userAnswer == null) {
                return score;
            }
            if (userAnswer.equals(questionRight)) {
                score = score + questionScore;
            }
        }
        double scoreFormatted =
                new BigDecimal(score)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
        return scoreFormatted;
    }

}
