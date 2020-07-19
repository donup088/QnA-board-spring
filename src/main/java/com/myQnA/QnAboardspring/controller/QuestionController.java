package com.myQnA.QnAboardspring.controller;

import com.myQnA.QnAboardspring.domain.Question;
import com.myQnA.QnAboardspring.domain.QuestionRepository;
import com.myQnA.QnAboardspring.domain.User;
import com.myQnA.QnAboardspring.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        Question newQuestion=new Question(sessionUser,title,content);

        System.out.println("content: "+content);

        questionRepository.save(newQuestion);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String showQuestion(@PathVariable Long id, Model model){
        Question question=questionRepository.findById(id).get();
        model.addAttribute("question",question);

        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateQuestionForm(@PathVariable Long id,Model model,HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "/users/loginForm";
        }

        User loginUser=HttpSessionUtils.getUserFromSession(session);
        Question question=questionRepository.findById(id).get();

        if(!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        model.addAttribute("question",question);
        return "/qna/updateForm";
    }

    @PutMapping("/{id}")
    public String updateQuestion(@PathVariable Long id,String title,String content,HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "/users/loginForm";
        }

        User loginUser=HttpSessionUtils.getUserFromSession(session);
        Question question=questionRepository.findById(id).get();

        if(!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        question.update(title,content);
        questionRepository.save(question);

        return String.format("redirect:/questions/%d",id);
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id,HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "/users/loginForm";
        }

        User loginUser=HttpSessionUtils.getUserFromSession(session);
        Question question=questionRepository.findById(id).get();

        if(!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        questionRepository.delete(question);

        return "redirect:/";
    }
}
