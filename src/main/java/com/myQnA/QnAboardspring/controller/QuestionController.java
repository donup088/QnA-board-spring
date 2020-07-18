package com.myQnA.QnAboardspring.controller;

import com.myQnA.QnAboardspring.domain.Question;
import com.myQnA.QnAboardspring.domain.QuestionRepository;
import com.myQnA.QnAboardspring.domain.User;
import com.myQnA.QnAboardspring.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("questions")
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/form")
    public String questionForm(HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "/users/loginForm";
        }
        return "/qna/form";
    }

    @PostMapping("")
    public String createQuestion(String title,String content,HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "/users/loginForm";
        }
        User sessionUser=HttpSessionUtils.getUserFromSession(session);

        Question newQuestion=new Question(sessionUser.getUserId(),title,content);

        questionRepository.save(newQuestion);
        return "redirect:/";
    }
}
