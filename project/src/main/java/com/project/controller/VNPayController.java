package com.project.controller;

import com.project.entity.*;
import com.project.entity.enum_entity.HandlerOrder;
import com.project.entity.enum_entity.PaymentMethod;
import com.project.entity.enum_entity.StatusOrder;
import com.project.response.VNPayResponse;
import com.project.service.SearchBookService;
import com.project.service.impl.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VNPayController {
    @Autowired
    private VNPayServiceImpl vnPayService;
    @Autowired
    private ManagerOrderServiceImpl managerOrderService;
    @Autowired
    private ManagerOrderItemServiceImpl managerOrderItemService;
    @Autowired
    private ManagerBookServiceImpl managerBookService;
    @Autowired
    private SearchBookServiceImpl service;

    @GetMapping("/checkout_after_payment")
    public String checkoutAfterPayment( @ModelAttribute VNPayResponse vnPayResponse, HttpSession httpSession, Model model){
        if (vnPayService.isSuccess(vnPayResponse)){
            Order order = (Order) httpSession.getAttribute("order");
            order.setStatusOrder(StatusOrder.APPROVED);
            order.setHandlerOrder(HandlerOrder.ACCEPT);
            managerOrderService.updateOrder(order);
            @SuppressWarnings("unchecked")
            List<OrderItem> orderItems = (List<OrderItem>) httpSession.getAttribute("orderItems");

            for (OrderItem oi : orderItems) {
                Book book = managerBookService.getBookById(oi.getBook().getBookId());
                book.setQuantity(book.getQuantity() - oi.getQuantityBuy());
                managerBookService.updateBook(book);

            }
            service.syncAllBooksToES();
            httpSession.removeAttribute("order");
            httpSession.removeAttribute("orderItems");

            return "success_payment";
        }else {
            Map<String, String> responseError = Map.of(
                    "11", "Giao dịch không thành công do: Đã hết hạn chờ thanh toán",
                    "13", "Giao dịch không thành công do: Nhập sai mã OTP",
                    "09", "Giao dịch không thành công do: Chưa đăng ký InternetBanking",
                    "24", "Giao dịch không thành công do: Khách hàng hủy giao dịch"
            );
            String response = responseError.get(vnPayResponse.getVnp_ResponseCode());
            model.addAttribute("errorResponse",response);
            return "fail_payment";
        }
    }


}
