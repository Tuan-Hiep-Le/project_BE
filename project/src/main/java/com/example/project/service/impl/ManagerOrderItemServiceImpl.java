package com.example.project.service.impl;

import com.example.project.entity.Order;
import com.example.project.entity.OrderItem;
import com.example.project.repository.ManagerOrderItemRepository;
import com.example.project.service.ManagerOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerOrderItemServiceImpl implements ManagerOrderItemService {
    @Autowired
    private ManagerOrderItemRepository managerOrderRepository;

    @Override
    public OrderItem addOrder(OrderItem orderItem) {
        return managerOrderRepository.saveAndFlush(orderItem);
    }
}
