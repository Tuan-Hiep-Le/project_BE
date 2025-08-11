package com.project.service;

import com.project.entity.OrderItem;
import org.springframework.data.jpa.repository.Query;


public interface ManagerOrderItemService {
    // Them don hang chi tiet
    public OrderItem addOrderItem(OrderItem orderItem);



}
