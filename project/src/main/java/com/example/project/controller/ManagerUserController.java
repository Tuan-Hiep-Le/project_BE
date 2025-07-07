package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.service.user_service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
public class ManagerUserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/login")
    public String userLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(@RequestParam("email_user")String email, @RequestParam("password_user") String password, Model model){
        boolean isLoginSuccess = userService.isLoginSuccess(email,password);
        model.addAttribute("is_login_success",isLoginSuccess);
        return "home_after_login";
    }

    @GetMapping("/register")
    public String userRegister(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String userRegister(@ModelAttribute("user") User user){
        userService.addUser(user);
        return "register_success";
    }






}
