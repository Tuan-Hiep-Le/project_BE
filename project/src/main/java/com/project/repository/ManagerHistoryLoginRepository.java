package com.project.repository;

import com.project.entity.LoginHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerHistoryLoginRepository extends JpaRepository<LoginHistory,Integer> {
    List<LoginHistory> findByUserUserId(Integer userId);

    @Query("SELECT u.userId, u.firstName, u.email, lh.loginTime\n" +
            "FROM LoginHistory lh\n" +
            "JOIN lh.user u WHERE u.userId = :userId\n"+
            "ORDER BY lh.loginTime DESC")
    public List<Object[]> getAllHistoryByUserId(@Param("userId") Integer userId);
}
