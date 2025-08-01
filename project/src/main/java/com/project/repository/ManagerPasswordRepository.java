package com.project.repository;

import com.project.entity.ResetPassword;
import com.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ManagerPasswordRepository extends JpaRepository<ResetPassword,Integer> {
    public ResetPassword findByResetPasswordToken(String resetPasswordToken);

    @Query("DELETE FROM ResetPassword rs WHERE rs.user = :user")
    @Modifying
    @Transactional
    public void removeByUser(@Param("user")User user);

}
