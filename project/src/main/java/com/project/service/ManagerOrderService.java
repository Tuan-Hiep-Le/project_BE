package com.project.service;

import com.project.entity.Order;
import com.project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public Page<Object[]> getHistoryBuyProduct(Pageable pageable);

    //Lấy ra tổng số đơn hàng
    public long countOrder();

    //Lấy ra tổng doanh thu
    public BigDecimal getTotalRevenue();

    //Lấy ra các thông tin cần thiết của đơn hàng
    public Page<Object[]> getInformationOrder(Pageable pageable);

    public Page<Object[]> getOrderNull(Pageable pageable);

    public Page<Object[]> getOrderAccept(Pageable pageable);

    public Page<Object[]> getOrderRefuse(Pageable pageable);

    public Page<Object[]> getOrderCash(Pageable pageable);

    public Page<Object[]> getOrderTransfer(Pageable pageable);


    //Lấy ra đơn hàng bằng Id
    public Order getOrderById(Integer orderId);


}
