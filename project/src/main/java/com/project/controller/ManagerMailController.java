package com.project.controller;

import com.project.entity.User;
import com.project.entity.VerifyEmail;
import com.project.service.impl.ManagerEmailServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManagerMailController {
    @Autowired
    private ManagerEmailServiceImpl managerEmailService;
    @GetMapping("/send_email")
    public String sendEmailToVerify(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("loggedUser");
        if (managerEmailService.isUserVerified(user)){
            model.addAttribute("userVerified",true);
        } else {
            managerEmailService.sendVerifyEmail(request);
        }

        return "verify_email";
    }

    @PostMapping("/verify_email_for_user")
    public String verifyByToken(@RequestParam("tokenVerify") String token, HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("loggedUser");
        if (managerEmailService.isUserVerified(user)){
            model.addAttribute("userVerified",true);
            return "verify_email";
        }
        VerifyEmail tokenVerify = managerEmailService.latestToken(user.getUserId());
        if ( !token.equals(tokenVerify.getVerificationTokenEmail())){
            model.addAttribute("isVerify",false);
            return "verify_email";
        }
        model.addAttribute("isVerify",true);
        model.addAttribute("tokenVerify",token);
        tokenVerify.setIsVerifiedEmail(true);
        managerEmailService.updateVerifiedEmail(tokenVerify);
        return "verify_success";
    }
}
