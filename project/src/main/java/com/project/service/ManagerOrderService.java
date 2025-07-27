package com.project.service;

import com.project.entity.Order;


public interface ManagerOrderService {
    // Them don hang
    public Order addOrder(Order order);

    ////Lấy ra order gần đây nhất của người dùng có mua cuốn sách này
    public Order getOrderByCondition(Integer userId, Integer idBook);

    //Chỉnh sửa đơn hàng
    public Order updateOrder(Order order);


}
