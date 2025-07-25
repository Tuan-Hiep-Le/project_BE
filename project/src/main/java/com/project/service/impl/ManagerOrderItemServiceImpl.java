package com.project.service.impl;

import com.project.entity.OrderItem;
import com.project.repository.ManagerOrderItemRepository;
import com.project.service.ManagerOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerOrderItemServiceImpl implements ManagerOrderItemService {
    @Autowired
    private ManagerOrderItemRepository managerOrderRepository;

    @Override
    public OrderItem addOrderItem(OrderItem orderItem) {
        return managerOrderRepository.saveAndFlush(orderItem);
    }
}
