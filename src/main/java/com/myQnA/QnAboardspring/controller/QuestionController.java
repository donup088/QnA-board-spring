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
            return "/user/loginForm";
        }
        return "/qna/form";
    }

    @PostMapping("")
    public String createQuestion(String title,String content,HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "/user/loginForm";
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
        Question question=questionRepository.findById(id).get();

        try{
            hasPermission(session,question);
            model.addAttribute("question",question);

            return "/qna/updateForm";
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());

            return "/user/loginForm";
        }
    }

    @PutMapping("/{id}")
    public String updateQuestion(@PathVariable Long id,String title,String content,HttpSession session,Model model){
        Question question=questionRepository.findById(id).get();

        try{
            hasPermission(session,question);
            question.update(title,content);
            questionRepository.save(question);

            return String.format("redirect:/questions/%d",id);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());

            return "/user/loginForm";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id,HttpSession session,Model model){
        Question question=questionRepository.findById(id).get();

        try{
            hasPermission(session,question);
            questionRepository.delete(question);

            return "redirect:/";
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());

            return "/user/loginForm";
        }
    }

    private void hasPermission(HttpSession session,Question question){
        if(!HttpSessionUtils.isLogin(session)){
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        User loginUser= HttpSessionUtils.getUserFromSession(session);

        if(!question.isSameWriter(loginUser)){
            throw new IllegalStateException("자신의 글만 수정할 수 있습니다.");
        }
    }
}
