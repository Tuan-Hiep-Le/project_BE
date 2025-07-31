package com.project.repository;

import com.project.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ManagerPasswordRepository extends JpaRepository<ResetPassword,Integer> {
    public ResetPassword findByResetPasswordToken(String resetPasswordToken);
    //Kiểm tra xem link đã hết hạn chưa
    @Query("SELECT ")

}
