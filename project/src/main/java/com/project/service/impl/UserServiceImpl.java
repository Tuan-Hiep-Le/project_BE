package com.project.service.impl;

import com.project.entity.User;
import com.project.entity.enum_entity.Role;
import com.project.entity.enum_entity.Status;
import com.project.repository.ManagerUserRepository;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ManagerUserRepository managerUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setStatus(Status.NOT_VERIFIED);
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

    @Override
    public boolean numberPhoneValid(String numberPhone) {
        if(isDigit(numberPhone)) {
            return (numberPhone.length() == 10 && numberPhone.charAt(0) == '0');
        }
        return false;
    }

    public boolean isDigit(String s){
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> optionalUser= managerUserRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {

            throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email);

        }
    }

    @Override
    public User updatePassword(User user) {
        Optional<User> optionalUser = managerUserRepository.findByEmail(user.getEmail());
        User userCurrent = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found"));
        userCurrent.setPassword(user.getPassword());
        return managerUserRepository.saveAndFlush(userCurrent);
    }
}
