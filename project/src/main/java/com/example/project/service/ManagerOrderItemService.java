package com.example.project.service;

import com.example.project.entity.Order;
import com.example.project.entity.OrderItem;
import org.springframework.stereotype.Service;


public interface ManagerOrderItemService {
    // Them don hang chi tiet
    public OrderItem addOrder(OrderItem orderItem);

}
