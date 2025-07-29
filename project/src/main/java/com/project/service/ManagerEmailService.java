package com.project.service;

import com.project.entity.User;
import com.project.entity.VerifyEmail;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

public interface ManagerEmailService {
    //Gửi email
    public VerifyEmail sendVerifyEmail(HttpServletRequest request);

    //Thêm dữ liệu vào database
    public VerifyEmail saveVerifyEmail(VerifyEmail verifyEmail);

    //update database
    public VerifyEmail updateVerifiedEmail(VerifyEmail verifyEmail);

    //Kiểm tra xem token đã tồn tại hay chưa
    public boolean isTokenExists(String token, LocalDateTime currentTime);

    //Lấy ra cái token mới nhất của người dùng
    public VerifyEmail latestToken(Integer userId);

    //Kiểm tra xem tài khoản  người ấy đã xác thực hay chưa
    public boolean isUserVerified(User user);


}
