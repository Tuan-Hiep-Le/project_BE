package com.project.service.impl;

import com.project.entity.Order;
import com.project.entity.User;
import com.project.repository.ManagerOrderRepository;
import com.project.service.ManagerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerOrderServiceImpl implements ManagerOrderService {
    @Autowired
    private ManagerOrderRepository managerOrderRepository;

    @Override
    public Order addOrder(Order order) {
        return managerOrderRepository.saveAndFlush(order);
    }

    @Override
    public Order getOrderByCondition(Integer userId, Integer idBook) {
        return managerOrderRepository.getOrderBuyBookLates(userId,idBook);
    }

    @Override
    public Order updateOrder(Order order) {
        Optional<Order> orderOptional = managerOrderRepository.findById(order.getOrderId());
        if (orderOptional.isPresent()) {
            Order orderConstain = orderOptional.get();
            orderConstain.setHandlerOrder(order.getHandlerOrder());
            orderConstain.setStatusOrder(order.getStatusOrder());
            managerOrderRepository.saveAndFlush(orderConstain);
            return orderConstain;
        }
        throw new RuntimeException("Đơn hàng không tồn tại! ");
    }

    @Override
    public List<Object[]> getHistoryBuyProduct() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return managerOrderRepository.findHistoryBuyProduct(user);
    }
}
