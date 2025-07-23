package com.example.project.service;

import com.example.project.entity.Order;
import org.springframework.stereotype.Service;


public interface ManagerOrderService {
    // Them don hang
    public Order addOrder(Order order);
    ////Lấy ra order gần đây nhất của người dùng có mua cuốn sách này
    public Order getOrderByCondition(Integer userId, Integer idBook);


}
