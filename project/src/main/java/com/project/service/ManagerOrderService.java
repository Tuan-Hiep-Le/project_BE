package com.project.service;

import com.project.entity.Order;
import com.project.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface ManagerOrderService {
    // Them don hang
    public Order addOrder(Order order);

    ////Lấy ra order gần đây nhất của người dùng có mua cuốn sách này
    public Order getOrderByCondition(Integer userId, Integer idBook);

    //Chỉnh sửa đơn hàng
    public Order updateOrder(Order order);

    //Lấy ra lịch sử mua hàng của người dùng
    public List<Object[]> getHistoryBuyProduct();

    //Lấy ra tổng số đơn hàng
    public long countOrder();

    //Lấy ra tổng doanh thu
    public BigDecimal getTotalRevenue();


}
