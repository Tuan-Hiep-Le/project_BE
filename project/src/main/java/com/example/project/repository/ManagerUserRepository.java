package com.example.project.repository;

import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ManagerUserRepository extends JpaRepository<User,Integer> {
    //Kiểm tra xem người dùng đã tn tại chưa
    public boolean existsByEmail(String email);

    //Tìm kiếm người dùng bằng email
    public Optional<User> findByEmail(String email);

}
