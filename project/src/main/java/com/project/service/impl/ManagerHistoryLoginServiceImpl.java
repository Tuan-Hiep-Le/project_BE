package com.project.service.impl;

import com.project.entity.LoginHistory;
import com.project.entity.User;
import com.project.repository.ManagerHistoryLoginRepository;
import com.project.service.ManagerHistoryLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerHistoryLoginServiceImpl implements ManagerHistoryLoginService {
    @Autowired
    private ManagerHistoryLoginRepository managerHistoryLoginRepository;
    @Override
    public LoginHistory addLoginHistory(LoginHistory loginHistory) {
        return managerHistoryLoginRepository.saveAndFlush(loginHistory);
    }

    @Override
    public List<LoginHistory> getLoginHistoryByUser(Integer userId) {
        return managerHistoryLoginRepository.findByUserUserId(userId);
    }

    @Override
    public List<Object[]> getAllHistoryLoginByUserId(Integer userId) {
        return managerHistoryLoginRepository.getAllHistoryByUserId(userId);
    }
}
