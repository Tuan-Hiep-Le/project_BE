package com.example.project.service.user_service;

import com.example.project.config.SecurityConfig;
import com.example.project.entity.User;
import com.example.project.repository.ManagerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private ManagerUserRepository managerUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return managerUserRepository.saveAndFlush(user);
    }

    @Override
    public boolean isExist(String email) {
        return managerUserRepository.existsByEmail(email);
    }

    @Override
    public boolean isLoginSuccess(String email, String password) {
        return  managerUserRepository.findByEmail(email).map(user -> passwordEncoder.matches(password, user.getPassword())).orElse(false);

    }
}
