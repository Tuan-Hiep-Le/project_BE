package com.project.repository;

import com.project.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerOrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
