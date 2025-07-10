package com.example.project.repository;

import com.example.project.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerOrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
