package com.project.service;

import com.project.entity.ResetPassword;
import com.project.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface ManagerPasswordService {
    //Thêm dữ liệu vào database
    public ResetPassword addResetPassword(ResetPassword resetPassword);

    //Cập nhật dữ liệu
    public ResetPassword updateResetPassword(ResetPassword resetPassword);

    //Gửi link thay đổi mật khẩu
    public String sendLinkChangePassword(String email, HttpServletRequest request);

    //Lấy ra người dùng thông qua token
    public User getUserByToken(String resetPasswordToken);

    //Kiểm tra xem token đã hết hạn hay chưa
    public boolean isTokenExpired(String resetPasswordToken);

    //Lấy ra ResetPassword thông qua token
    public ResetPassword getByToken(String token);

}
