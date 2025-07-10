package com.example.project.repository;

import com.example.project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerOrderRepository extends JpaRepository<Order,Integer> {

}
