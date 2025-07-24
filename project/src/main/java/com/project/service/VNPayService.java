package com.project.service;

import com.project.entity.OrderItem;
import com.project.response.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;

public interface VNPayService {
    //Tạo thanh toán
    public String createPayment(HttpServletRequest request, BigDecimal amount, List<OrderItem> orderItems);

    //Kiểm tra xem thanh toán đã thành công hay chưa
    public boolean isSuccess(VNPayResponse vnPayResponse);
}
