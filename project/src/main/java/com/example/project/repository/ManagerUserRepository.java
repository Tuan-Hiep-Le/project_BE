package com.example.project.repository;

import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerUserRepository extends JpaRepository<User,Integer> {
    //Tìm người dùng bằng Id
    public User findByUserId(Integer id_user);
}
