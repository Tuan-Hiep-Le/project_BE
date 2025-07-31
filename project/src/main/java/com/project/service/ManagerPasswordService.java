package com.project.service;

import com.project.entity.ResetPassword;
import com.project.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface ManagerPasswordService {
    //Thêm dữ liệu vào database
    public ResetPassword addResetPassword(ResetPassword resetPassword);

    //Gửi link thay đổi mật khẩu
    public ResetPassword sendLinkChangePassword(String email);

    //Lấy ra người dùng thông qua token
    public User getUserByToken(String resetPasswordToken);
}
