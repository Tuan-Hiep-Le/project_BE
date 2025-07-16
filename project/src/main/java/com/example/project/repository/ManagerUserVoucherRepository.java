package com.example.project.repository;

import com.example.project.entity.UserVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerUserVoucherRepository extends JpaRepository<UserVoucher,Integer> {
    //Lấy ra tất cả các voucher của một người dùng
    @Query("SELECT us.voucher, (us.quantity -us.quantityUsed) as remainQuantity\n" +
            "FROM UserVoucher us\n" +
            "WHERE us.user.userId = :id AND CURRENT_DATE BETWEEN us.voucher.startTime AND us.voucher.endTime")
    public List<Object[]> getAllVoucherById(@Param("id") Integer id_user);


}
