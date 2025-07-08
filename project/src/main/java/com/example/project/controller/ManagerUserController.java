package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.entity.enum_entity.Role;
import com.example.project.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ManagerUserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SecurityContextRepository securityContextRepository;

    @GetMapping("/login")
    public String userLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(@RequestParam("email_user")String email, @RequestParam("password_user") String password, Model model, HttpServletRequest request, HttpServletResponse response){
        if (! userService.isLoginSuccess(email,password)){
            model.addAttribute("isLoginSuccess",false);
            return "login";
        }
        model.addAttribute("isLoginSuccess",true);
        User user = userService.getUserByEmail(email);
        List<SimpleGrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole()));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null,authorityList);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        if (user.getRole().equals(Role.USER)){
            return "redirect:/home_user_after_login";
        }
        return "redirect:/homepage"; // hoặc bất kỳ trang mặc định nào

    }

    @GetMapping("/register")
    public String userRegister(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String userRegister(@ModelAttribute("user") User user, @RequestParam("rePassword") String rePassword, Model model){
        model.addAttribute("user", user);
        boolean hasError = false;
        if (userService.isExist(user.getEmail())){
            model.addAttribute("isEmailExist",userService.isExist(user.getEmail()));
            hasError = true;
        }

        if(!userService.numberPhoneValid(user.getPhoneNumber())){
            model.addAttribute("isNumberPhoneValid",false);
            hasError = true;
        }

        if(!(rePassword.equals(user.getPassword())))  {
            model.addAttribute("isValidPassword",false);
            hasError = true;

        }
        if (hasError) {
            return "register";
        }
        userService.addUser(user);
        return "register_success";
    }









}
