package com.project.controller;

import com.project.entity.LoginHistory;
import com.project.entity.User;
import com.project.entity.enum_entity.Role;
import com.project.service.impl.ManagerHistoryLoginServiceImpl;
import com.project.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Controller
public class ManagerUserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SecurityContextRepository securityContextRepository;
    @Autowired
    private ManagerHistoryLoginServiceImpl managerHistoryLoginService;

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
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", context);
        request.getSession().setAttribute("loggedUser",user);
        LoginHistory loginHistory = LoginHistory.builder().user(user).loginTime(LocalDateTime.now()).build();
        managerHistoryLoginService.addLoginHistory(loginHistory);
        if (user.getRole().equals(Role.USER)){
            return "redirect:/homepage";
        }
        return "redirect:/admin";

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

    //Đăng xuất
    @GetMapping("/logout")
    public String userLogOut(HttpServletRequest request, HttpSession httpSession){
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        httpSession.invalidate();
        return "redirect:/home_user_after_login";
    }

    @GetMapping("/homepage/move_edit_personal")
    public String moveToEditPersonal(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("loggedUser");
        if(user.getAvatar() == null) {
            model.addAttribute("hasAvatar",false);
        }else {
            model.addAttribute("hasAvatar",true);
        }
        model.addAttribute("user",user);
        return "edit_personal";
    }

    @PostMapping("/homepage/edit_personal")
    public String editPersonalInformation(@ModelAttribute User user, HttpServletRequest request){
        User userUpdate = userService.updateUser(user);
        request.getSession().setAttribute("loggedUser",userUpdate);
        return "redirect:/home_after_user_login/move_edit_personal";
    }

    @PostMapping("/upload_avatar")
    public String uploadAvatar(@RequestParam("avatar")MultipartFile multipartFile, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("loggedUser");
        Path projectPath = Paths.get("").toAbsolutePath();
        Path uploadPath = projectPath.resolve("project").resolve("uploads");

        System.out.println("Đường dẫn upload: " + uploadPath.toAbsolutePath());

        if (!multipartFile.isEmpty()) {
            try {

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = "avatar_"+user.getUserId()+"_"+System.currentTimeMillis()+".jpg";
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath,multipartFile.getBytes());
                user.setAvatar("/uploads/"+fileName);
                User userUpdate = userService.updateUser(user);
                request.getSession().setAttribute("loggedUser",userUpdate);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return "redirect:/home_after_user_login/move_edit_personal";
    }

    @GetMapping("/return_home")
    public String returnHome(){
        return "redirect:/home_user_after_login";
    }

    @GetMapping("/histories_login")
    public String moveHistoryLogin(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("loggedUser");
        List<Object[]>  list = managerHistoryLoginService.getAllHistoryLoginByUserId(user.getUserId());
        model.addAttribute("listHistoryLogin",list);
        return "history_login_page";
    }











}
