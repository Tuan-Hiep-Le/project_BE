package com.example.project.repository;

import com.example.project.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerVoucherRepository extends JpaRepository<Voucher,Integer> {
    //Lay ra tat ca voucher TRANSFER ma nguoi do so hu
}
