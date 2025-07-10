package com.example.project.service.impl;

import com.example.project.entity.Order;
import com.example.project.repository.ManagerOrderRepository;
import com.example.project.service.ManagerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerOrderServiceImpl implements ManagerOrderService {
    @Autowired
    private ManagerOrderRepository managerOrderRepository;

    @Override
    public Order addOrder(Order order) {
        return managerOrderRepository.saveAndFlush(order);
    }
}
