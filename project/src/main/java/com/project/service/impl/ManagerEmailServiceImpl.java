package com.project.service.impl;

import com.project.entity.User;
import com.project.entity.VerifyEmail;
import com.project.repository.ManagerEmailRepository;
import com.project.service.ManagerEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class ManagerEmailServiceImpl implements ManagerEmailService {
    @Autowired
    private ManagerEmailRepository managerEmailRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired

    private TemplateEngine templateEngine;
    @Override
    @Transactional
    public VerifyEmail sendVerifyEmail(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("loggedUser");
        int token;
        do{
            token = createToken();

        }while (managerEmailRepository.existsByVerificationTokenEmailAndIsVerifiedEmailFalseAndTokenLifeTimeAfter(String.valueOf(token),LocalDateTime.now()));

        LocalDateTime tokenLifeEmail = LocalDateTime.now().plusMinutes(15);
        VerifyEmail verifyEmail = VerifyEmail.builder().user(user).verificationTokenEmail(String.valueOf(token)).tokenLifeTime(tokenLifeEmail).isVerifiedEmail(false).build();
        Context context = new Context();
        context.setVariable("name",user.getFirstName());
        context.setVariable("token",token);
        String htmlContent = templateEngine.process("email_verification",context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
        try {
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Xác thực email");
            messageHelper.setText(htmlContent,true);
            javaMailSender.send(mimeMessage);

        }catch (MessagingException ex){
            throw new RuntimeException("Gửi email thất bại", ex);
        }

        return saveVerifyEmail(verifyEmail);


    }
    private int createToken(){
        SecureRandom random = new SecureRandom();
        return  10000000+ random.nextInt(90000000);
    }

    @Override
    public VerifyEmail saveVerifyEmail(VerifyEmail verifyEmail) {
        return managerEmailRepository.saveAndFlush(verifyEmail);
    }

    @Override
    public boolean isTokenExists(String token, LocalDateTime currentTime) {
        return managerEmailRepository.existsByVerificationTokenEmailAndIsVerifiedEmailFalseAndTokenLifeTimeAfter(token,LocalDateTime.now());
    }

    @Override
    public VerifyEmail latestToken(Integer userId) {
        return managerEmailRepository.getLastToken(userId);
    }

    @Override
    public boolean isUserVerified(User user) {
        return managerEmailRepository.existsByUserAndIsVerifiedEmailTrue(user);
    }

    @Override
    public VerifyEmail updateVerifiedEmail(VerifyEmail verifyEmail) {
        VerifyEmail verifyOfUser = managerEmailRepository.getLastToken(verifyEmail.getUser().getUserId());
        verifyOfUser.setIsVerifiedEmail(verifyEmail.getIsVerifiedEmail());
        return managerEmailRepository.saveAndFlush(verifyOfUser);
    }
}
