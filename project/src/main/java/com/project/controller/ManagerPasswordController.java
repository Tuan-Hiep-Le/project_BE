package com.project.controller;

import com.project.entity.User;
import com.project.service.impl.ManagerPasswordServiceImpl;
import com.project.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManagerPasswordController {
    @Autowired
    private ManagerPasswordServiceImpl managerPasswordService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired

    private PasswordEncoder  passwordEncoder;
    @GetMapping("/input_email_reset")
    public String switchInputEmail(){
        return "input_email_reset";
    }
    @GetMapping("/send_link")
    public String sendLinkChangePassword(@RequestParam("emailReset") String email, Model model){
        model.addAttribute("emailReset",email);
        if (!userService.isExist(email)){
            model.addAttribute("isExistEmail",false);
            return "input_email_reset";
        }
        managerPasswordService.sendLinkChangePassword(email);
        return "notification_password";
    }
    @GetMapping("/forget_password")
    public String changePassword(@RequestParam("token") String token, Model model){
        model.addAttribute("token",token);
        return "input_new_password";
    }

    @PostMapping("/input_new_password")
    public String inputNewPassword(@RequestParam("token") String token,@RequestParam("newPassword")String newPassword, @RequestParam("reNewPassword") String reNewPassword,Model model){
        if(!reNewPassword.equals(newPassword)){
            model.addAttribute("validPassword",false);
            return "input_new_password";
        }
        String newPasswordEncode =  passwordEncoder.encode(newPassword);
        User user = managerPasswordService.getUserByToken(token);
        user.setPassword(newPasswordEncode);
        userService.updatePassword(user);
        return "reset_password_success";



    }


}
