package com.project.service;

import com.project.entity.LoginHistory;
import com.project.entity.User;

import java.util.List;

public interface ManagerHistoryLoginService {
    //Lưu lịch sử người dùng
    public LoginHistory addLoginHistory(LoginHistory loginHistory);
    //Lấy ra lịch sử dăng nhập của người dùng
    public List<LoginHistory> getLoginHistoryByUser(Integer IdUser);

    public List<Object[]> getAllHistoryLoginByUserId(Integer userId);
}
