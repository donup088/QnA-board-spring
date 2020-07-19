package com.myQnA.QnAboardspring.controller;

import com.myQnA.QnAboardspring.domain.Answer;
import com.myQnA.QnAboardspring.domain.AnswerRepository;
import com.myQnA.QnAboardspring.domain.Question;
import com.myQnA.QnAboardspring.domain.QuestionRepository;
import com.myQnA.QnAboardspring.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("")
    public String answerCreate(@PathVariable Long questionId, String content, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "/users/loginForm";
        }

        Question question=questionRepository.findById(questionId).get();
        Answer answer=new Answer(HttpSessionUtils.getUserFromSession(session),question,content);

        answerRepository.save(answer);

        return String.format("redirect:/questions/%d",questionId);
    }

}
