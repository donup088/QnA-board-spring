package com.myQnA.QnAboardspring.controller;

import com.myQnA.QnAboardspring.domain.User;
import com.myQnA.QnAboardspring.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/form")
    public String form(){
        return "/user/form";
    }

    @PostMapping("")
    public String create(User user){
        System.out.println("User : "+user);
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model){
        model.addAttribute("users",userRepository.findAll());

        return "/user/list";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id,Model model,HttpSession session){
        User sessionUser= (User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/user/loginForm";
        }

        if(!id.equals(sessionUser.getId())){
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User user=userRepository.findById(id).get();
        model.addAttribute("user",user);

        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id,User updateUser,HttpSession session){
        User sessionUser= (User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/user/loginForm";
        }

        if(!id.equals(sessionUser.getId())){
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User user=userRepository.findById(id).get();
        user.update(updateUser);
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "/user/loginForm";
    }

    @PostMapping("/login")
    public String login(String userId, String password, HttpSession session){
        User user=userRepository.findByUserId(userId);
        if(user==null){
            return "redirect:/users/loginForm";
        }
        if(!password.equals(user.getPassword())){
            return "redirect:/users/loginForm";
        }
        session.setAttribute("sessionUser",user);

        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.removeAttribute("sessionUser");

        return "redirect:/";
    }
}
