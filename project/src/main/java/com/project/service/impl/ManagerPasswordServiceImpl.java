package com.project.service.impl;

import com.project.entity.ResetPassword;
import com.project.entity.User;
import com.project.repository.ManagerPasswordRepository;
import com.project.service.ManagerPasswordService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ManagerPasswordServiceImpl implements ManagerPasswordService {
    @Autowired
    private ManagerPasswordRepository managerPasswordRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public ResetPassword addResetPassword(ResetPassword resetPassword) {
        return managerPasswordRepository.saveAndFlush(resetPassword);
    }

    @Override
    @Transactional
    public ResetPassword sendLinkChangePassword(String email) {
        User user = userService.getUserByEmail(email);
        String token = UUID.randomUUID().toString();
        ResetPassword resetPassword = ResetPassword.builder().user(user).resetPasswordToken(token).resetTokenExpiry(LocalDateTime.now().plusMinutes(15)).build();
        Context context = new Context();
        String links = "http://localhost:8080/forgot_password?token="+token;
        context.setVariable("name",user.getFirstName());
        context.setVariable("linkToken",links);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
        String htmlResetPassword = templateEngine.process("link_change_password",context);
        try {
            messageHelper.setTo(email);
            messageHelper.setSubject("Thay đổi mật khẩu");
            messageHelper.setText(htmlResetPassword,true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException ex){
            throw new RuntimeException("Gửi link thất bại" + ex);
        }
        return managerPasswordRepository.saveAndFlush(resetPassword);
    }

    @Override
    public User getUserByToken(String resetPasswordToken) {
        ResetPassword resetPassword = managerPasswordRepository.findByResetPasswordToken(resetPasswordToken);
        if(resetPassword == null){
            throw new RuntimeException("Không tìm thấy người dùng");
        }
        return resetPassword.getUser();
    }
}
