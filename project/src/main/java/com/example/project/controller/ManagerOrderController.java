package com.example.project.controller;

import com.example.project.entity.*;
import com.example.project.entity.elastic.ShipCostDocument;
import com.example.project.entity.enum_entity.HandlerOrder;
import com.example.project.entity.enum_entity.PaymentMethod;
import com.example.project.entity.enum_entity.StatusOrder;
import com.example.project.entity.enum_entity.TypeVoucher;

import com.example.project.service.ManagerOrderItemService;
import com.example.project.service.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ManagerUserVoucherServiceImpl managerUserVoucherService;
    @Autowired
    private ManagerOrderItemService managerOrderItemService;
    @GetMapping("/admin/sync-shipcost")
    public String syncDataShipCostToElasticsearch(Model model) {
        searchShipCostService.syncShipCost();
        model.addAttribute("message", "Đã đồng bộ dữ liệu phí vận chuyển lên Elasticsearch thành công!");
        return "redirect:/homepage";
    }
    @GetMapping("/home_after_user_login/switch_buy_now")
    public String switchToBuyNow(@RequestParam("bookId") Integer id,Model model){
        Book book = managerBookService.getBookById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        int defaultQuantity = 1;
        model.addAttribute("bookId",id);
        BigDecimal initialTotalPrice = book.getPrice().multiply(BigDecimal.valueOf(defaultQuantity));
        model.addAttribute("totalPrice",initialTotalPrice);
        model.addAttribute("moneyShip", 0);
        model.addAttribute("payment", initialTotalPrice);
        model.addAttribute("bookId", id);
        model.addAttribute("bookBuy", book);
        model.addAttribute("quantityBuy", defaultQuantity);
        List<Object[]> listVoucherTransfer = managerUserVoucherService.getAllVoucherTransfer(user.getUserId());
        List<Object[]> listVoucherDiscount = managerUserVoucherService.getAllVoucherDiscount(user.getUserId());
        model.addAttribute("listVoucherTransfer",listVoucherTransfer);
        model.addAttribute("listVoucherDiscount",listVoucherDiscount);
        return "buy_book_now";
    }
    //Mua ngay san pham
    @PostMapping("/home_after_user_login/buy_now")
    public String createOrderItem(@RequestParam("bookId") Integer id, @RequestParam(value = "quantityBuy") int quantityBuy, @RequestParam("where") String address,@RequestParam(value = "valueVoucherTransfer",required = false) Integer idTransfer,@RequestParam(value = "valueVoucherDiscount",required = false) Integer idDiscount,
                                  @RequestParam(value = "paymentMethod", defaultValue = "CASH")PaymentMethod paymentMethod, @RequestParam(value = "handleOrder",required = false) HandlerOrder handlerOrder,Model model){
        Book book = managerBookService.getBookById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        BigDecimal totalPrice = book.getPrice().multiply(BigDecimal.valueOf(quantityBuy));
        model.addAttribute("totalPrice",totalPrice);
        String[] addressSplit = address.split("-");
        String addressShip = addressSplit[addressSplit.length - 1];
        ShipCostDocument shipCostDocument = searchShipCostService.getShipCostByCity(addressShip);
        model.addAttribute("moneyShip",BigDecimal.valueOf(shipCostDocument.getCost()));

        List<Object[]> listVoucherTransfer = managerUserVoucherService.getAllVoucherTransfer(user.getUserId());
        List<Object[]> listVoucherDiscount = managerUserVoucherService.getAllVoucherDiscount(user.getUserId());

        List<Voucher> voucherList = new ArrayList<>();
        for (Object[] voucher : listVoucherTransfer){
            Voucher voucherTransfer = (Voucher) voucher[0];
            if (voucherTransfer.getVoucherCode() == idTransfer){
                voucherList.add(voucherTransfer);
            }
        }

        for (Object[] voucher : listVoucherDiscount){
            Voucher voucherDiscount = (Voucher) voucher[0];
            if (voucherDiscount.getVoucherCode() == idDiscount){
                voucherList.add(voucherDiscount);
            }
        }


        BigDecimal payment = totalPrice.add(BigDecimal.valueOf(shipCostDocument.getCost()));
        if (!voucherList.isEmpty() ){
            for (Voucher voucher : voucherList) {
                if(voucher.getTypeVoucher().equals(TypeVoucher.DISCOUNT)){
                    BigDecimal cost = BigDecimal.valueOf(shipCostDocument.getCost());
                    BigDecimal discountPercent = BigDecimal.valueOf(voucher.getValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    BigDecimal discountAmount = cost.multiply(discountPercent);
                    payment = payment.subtract(discountAmount);

                }else if (voucher.getTypeVoucher().equals(TypeVoucher.TRANSPORT)){
                    BigDecimal discountTransportPercent = BigDecimal.valueOf(voucher.getValue())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    BigDecimal discountTransport = totalPrice.multiply(discountTransportPercent);
                    payment = payment.subtract(discountTransport);
                }
            }
        }
        ShipCost shipCost = managerShipCostService.getShipCostById(shipCostDocument.getShipCostId());
        Order order ;
        if(handlerOrder == null) {
             order = Order.builder().user(user).totalPrice(totalPrice).shipCost( shipCost).voucherList(voucherList).paymentMethod(paymentMethod).statusOrder(StatusOrder.APPROVING).handlerOrder(null).payment(payment).buyAt(LocalDateTime.now()).build();

            //managerOrderService.addOrder(order);
        }else {
            StatusOrder statusOrder = (handlerOrder==HandlerOrder.ACCEPT)? StatusOrder.APPROVED: StatusOrder.CANCELED;
             order = Order.builder().user(user).totalPrice(totalPrice).shipCost( shipCost).voucherList(voucherList).paymentMethod(paymentMethod).statusOrder(statusOrder).handlerOrder(handlerOrder).payment(payment).buyAt(LocalDateTime.now()).build();
            managerOrderService.addOrder(order);
        }
        OrderItem orderItem = OrderItem.builder().order(order).book(book).quantityBuy(quantityBuy).totalPrice(totalPrice).build();

        //managerOrderItemService.addOrder(orderItem);
        model.addAttribute("bookId",id);
        model.addAttribute("valueVoucherTransfer",idTransfer);
        model.addAttribute("valueVoucherDiscount",idDiscount);
        model.addAttribute("where",address);
        model.addAttribute("bookBuy",book);
        model.addAttribute("paymentMethod",paymentMethod);
        model.addAttribute("payment",payment);
        model.addAttribute("quantityBuy", quantityBuy);


        return "buy_book_now";
    }

    @GetMapping("/get_shipcost")
    @ResponseBody
    public Map<String,Object> getShipCostByAddress(@RequestParam("where") String address){
        String[] addressSplit = address.split("-");
        String addressShip = addressSplit[addressSplit.length - 1];
        Map<String,Object> map = new HashMap<>();
        try {
            ShipCostDocument shipCostDocument = searchShipCostService.getShipCostByCity(addressShip);
            map.put("cost",shipCostDocument.getCost());

        }catch (Exception e){
            map.put("cost", "Not Found");
        }
        return map;
    }

}
