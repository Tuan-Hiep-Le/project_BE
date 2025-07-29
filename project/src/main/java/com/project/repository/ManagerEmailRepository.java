package com.project.repository;

import com.project.entity.User;
import com.project.entity.VerifyEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ManagerEmailRepository extends JpaRepository<VerifyEmail, Integer> {
    //Lấy ra token mới nhất của người dùng
    @Query(value = "SELECT ve.*\n" +
            "FROM verify_emails ve\n" +
            "WHERE ve.id_user = :idUser \n" +
            "AND ve.token_lifetime > NOW()\n"+
            "ORDER BY ve.token_lifetime DESC\n" +
            "LIMIT 1\n",nativeQuery = true)
    public VerifyEmail getLastToken(@Param("idUser") Integer idUser);

    //Kiểm tra xem token đã tồn tại hay chưa
    public boolean existsByVerificationTokenEmailAndIsVerifiedEmailFalseAndTokenLifeTimeAfter(String token, LocalDateTime currentTime);

    //Kiểm tra xem tài khoản  người ấy đã xác thực hay chưa
    public boolean existsByUserAndIsVerifiedEmailTrue(User user);
}
