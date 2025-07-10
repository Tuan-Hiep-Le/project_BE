package com.example.project.controller;

import com.example.project.entity.*;
import com.example.project.entity.enum_entity.HandlerOrder;
import com.example.project.entity.enum_entity.PaymentMethod;
import com.example.project.entity.enum_entity.StatusOrder;
import com.example.project.entity.enum_entity.TypeVoucher;
import com.example.project.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ManagerOrderController {
    @Autowired
    private ManagerBookServiceImpl managerBookService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SearchShipCostServiceImpl searchShipCostService;
    @Autowired
    private ManagerShipCostServiceImpl managerShipCostService;
    @Autowired
    private ManagerOrderServiceImpl managerOrderService;

    @GetMapping("/admin/sync-shipcost")
    public String syncDataShipCostToElasticsearch(Model model) {
        searchShipCostService.syncShipCost();
        model.addAttribute("message", "Đã đồng bộ dữ liệu phí vận chuyển lên Elasticsearch thành công!");
        return "redirect:/homepage";
    }
    @GetMapping("/home_after_user_login/switch_buy_now")
    public String switchToBuyNow(@RequestParam("bookId") Integer id,Model model){
        Book book = managerBookService.getBookById(id);
        model.addAttribute("bookId",id);
        model.addAttribute("book",book);
        return "buy_book_now";
    }
    //Mua ngay san pham
    @PostMapping("/home_after_user_login/buy_now")
    public String createOrderItem(@RequestParam("bookid") Integer id_book, @RequestParam(value = "quantityBuy",defaultValue = "1") int quantityBuy, @RequestParam("where") String address, @RequestParam("voucher")List<Voucher> voucherList,
                                  @RequestParam(value = "payment", defaultValue = "CASH")PaymentMethod paymentMethod, @RequestParam(value = "handleOrder",required = false) HandlerOrder handlerOrder){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =  userService.getUserByEmail(email);
        Book book =  managerBookService.getBookById(id_book);
        LocalDateTime orderAt = LocalDateTime.now();
        BigDecimal totalPrice = book.getPrice().multiply(BigDecimal.valueOf(quantityBuy));
        ShipCost shipCost = managerShipCostService.getShipCostById(Integer.getInteger(searchShipCostService.getShipCostByCity(address)));

        BigDecimal payment = totalPrice.add(BigDecimal.valueOf(shipCost.getCost()));
        if (!voucherList.isEmpty() ){
            for (Voucher voucher : voucherList) {
                if(voucher.getTypeVoucher().equals(TypeVoucher.DISCOUNT)){
                    BigDecimal cost = BigDecimal.valueOf(shipCost.getCost());
                    BigDecimal discountPercent = BigDecimal.valueOf(voucher.getValue()).divide(BigDecimal.valueOf(100), 2);
                    BigDecimal discountAmount = cost.multiply(discountPercent);
                    payment = payment.subtract(discountAmount);

                }else if (voucher.getTypeVoucher().equals(TypeVoucher.TRANSPORT)){
                    payment = payment.subtract(totalPrice.multiply(BigDecimal.valueOf(voucher.getValue()/100)));
                }
            }
        }
        Order order = null;
        if(handlerOrder == null) {
             order = Order.builder().user(user).totalPrice(totalPrice).shipCost(shipCost).paymentMethod(paymentMethod).statusOrder(StatusOrder.APPROVING).handlerOrder(null).payment(payment).build();
            managerOrderService.addOrder(order);
        }else {
            StatusOrder statusOrder = (handlerOrder==HandlerOrder.ACCEPT)? StatusOrder.APPROVED: StatusOrder.CANCELED;
             order = Order.builder().user(user).totalPrice(totalPrice).shipCost(shipCost).paymentMethod(paymentMethod).statusOrder(statusOrder).handlerOrder(handlerOrder).payment(payment).build();
            managerOrderService.addOrder(order);
        }
        OrderItem orderItem = OrderItem.builder().order(order).book(book).quantityBuy(quantityBuy).totalPrice(totalPrice).build();
        return "buy_book_now";
    }

}
