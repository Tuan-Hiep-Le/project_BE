package com.project.controller;

import com.project.entity.*;
import com.project.entity.enum_entity.HandlerOrder;
import com.project.entity.enum_entity.PaymentMethod;
import com.project.entity.enum_entity.StatusOrder;
import com.project.response.VNPayResponse;
import com.project.service.impl.ManagerBookServiceImpl;
import com.project.service.impl.ManagerOrderItemServiceImpl;
import com.project.service.impl.ManagerOrderServiceImpl;
import com.project.service.impl.VNPayServiceImpl;
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

    @GetMapping("/checkout_after_payment")
    public String checkoutAfterPayment( @ModelAttribute VNPayResponse vnPayResponse, HttpSession httpSession, Model model){
        if (vnPayService.isSuccess(vnPayResponse)){
//            User user = (User) httpSession.getAttribute("user");
//            BigDecimal totalPrice = (BigDecimal) httpSession.getAttribute("totalPrice");
//            ShipCost shipCost = (ShipCost) httpSession.getAttribute("shipCost");
//            @SuppressWarnings("unchecked")
//            List<Voucher> voucherList = (List<Voucher>) httpSession.getAttribute("voucherList");
//            @SuppressWarnings("unchecked")
//            List<Integer> quantityBuys = (List<Integer>) httpSession.getAttribute("quantityBuys");
//            @SuppressWarnings("unchecked")
//            List<Book> bookList = (List<Book>) httpSession.getAttribute("books");
//            BigDecimal payment = (BigDecimal) httpSession.getAttribute("payment");
//            Order order = Order.builder().user(user).totalPrice(totalPrice).shipCost( shipCost).voucherList(voucherList).paymentMethod(PaymentMethod.CASH).statusOrder(StatusOrder.APPROVED).handlerOrder(HandlerOrder.ACCEPT).payment(payment).buyAt(LocalDateTime.now()).build();
//            managerOrderService.addOrder(order);
//            for (int i = 0; i < bookList.size(); i++) {
//                OrderItem orderItem = OrderItem.builder().order(order).book(bookList.get(i)).quantityBuy(quantityBuys.get(i)).totalPrice(totalPrice).build();
//                bookList.get(i).setQuantity(bookList.get(i).getQuantity() - quantityBuys.get(i));
//                managerBookService.updateBook(bookList.get(i));
//                managerOrderItemService.addOrderItem(orderItem);
//
//            }
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
