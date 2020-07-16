package com.myQnA.QnAboardspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/hello")
    public String welcome(String name, Model model){
        System.out.println("name: "+name);
        model.addAttribute("name",name);
        return "welcome";
    }
}
