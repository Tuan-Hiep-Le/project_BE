package com.project.controller;

import com.project.entity.*;
import com.project.entity.elastic.ShipCostDocument;
import com.project.entity.enum_entity.HandlerOrder;
import com.project.entity.enum_entity.PaymentMethod;
import com.project.entity.enum_entity.StatusOrder;
import com.project.entity.enum_entity.TypeVoucher;

import com.project.service.ManagerOrderItemService;
import com.project.service.impl.*;
import com.project.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private VNPayServiceImpl vnPayService;
    @Autowired
    private ManagerCartItemServiceImpl managerCartItemService;
    @GetMapping("/admin/sync-shipcost")
    public String syncDataShipCostToElasticsearch(Model model) {
        searchShipCostService.syncShipCost();
        model.addAttribute("message", "Đã đồng bộ dữ liệu phí vận chuyển lên Elasticsearch thành công!");
        return "redirect:/homepage";
    }
    @GetMapping("/homepage/switch_buy_now")
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
        model.addAttribute("moneyShip", BigDecimal.ZERO);
        model.addAttribute("payment", initialTotalPrice);
        model.addAttribute("bookBuy", book);
        model.addAttribute("quantityBuy", defaultQuantity);
        List<Object[]> listVoucherTransfer = managerUserVoucherService.getAllVoucherTransfer(user.getUserId());
        List<Object[]> listVoucherDiscount = managerUserVoucherService.getAllVoucherDiscount(user.getUserId());
        model.addAttribute("listVoucherTransfer",listVoucherTransfer);
        model.addAttribute("listVoucherDiscount",listVoucherDiscount);
        return "buy_book_now";
    }
    //Mua ngay san pham
    @PostMapping("/homepage/buy_now")
    public String createOrderItem(@RequestParam("bookId") Integer id, @RequestParam(value = "quantityBuy") int quantityBuy, @RequestParam("where") String address, @RequestParam(value = "valueVoucherTransfer",required = false) Integer idTransfer, @RequestParam(value = "valueVoucherDiscount",required = false) Integer idDiscount,
                                  @RequestParam(value = "paymentMethod")PaymentMethod paymentMethod, Model model, HttpServletRequest request, HttpSession httpSession){

        Book book = managerBookService.getBookById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        BigDecimal totalPrice = book.getPrice().multiply(BigDecimal.valueOf(quantityBuy));
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("quantityBuy", quantityBuy);
        model.addAttribute("bookId",id);
        String[] addressSplit = address.split("-");
        String addressShip = addressSplit[addressSplit.length - 1];
        model.addAttribute("bookBuy", book);
        ShipCostDocument shipCostDocument;
        try {
            shipCostDocument = searchShipCostService.getShipCostByCity(addressShip);
            if (shipCostDocument == null) {
                model.addAttribute("errorMessage", "Không tìm thấy phí vận chuyển cho địa chỉ: " + addressShip);
                model.addAttribute("moneyShip",BigDecimal.ZERO);
                return "buy_book_now";
            }
        } catch (Exception e) {

            model.addAttribute("errorMessage", "Có lỗi xảy ra khi tính phí vận chuyển. Vui lòng thử lại sau.");
            model.addAttribute("moneyShip",BigDecimal.ZERO);

            return "buy_book_now";
        }

        String addressDelivery = shipCostDocument.getNameCity();

        StringBuilder addressBuilder = new StringBuilder();
        for (int i = 0; i < addressSplit.length - 1; i++){
            addressBuilder.append(addressSplit[i]+"-");
        }
        addressBuilder.append(addressDelivery);
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
                if(voucher.getTypeVoucher().equals(TypeVoucher.TRANSPORT)){
                    BigDecimal cost = BigDecimal.valueOf(shipCostDocument.getCost());
                    BigDecimal discountPercent = BigDecimal.valueOf(voucher.getValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    BigDecimal discountAmount = cost.multiply(discountPercent);
                    payment = payment.subtract(discountAmount);

                }else if (voucher.getTypeVoucher().equals(TypeVoucher.DISCOUNT)){
                    BigDecimal discountTransportPercent = BigDecimal.valueOf(voucher.getValue())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    BigDecimal discountTransport = totalPrice.multiply(discountTransportPercent);
                    payment = payment.subtract(discountTransport);
                }
            }
        }
        ShipCost shipCost = managerShipCostService.getShipCostById(shipCostDocument.getShipCostId());
        Order order = Order.builder().user(user).totalPrice(totalPrice).shipCost( shipCost).voucherList(voucherList).paymentMethod(paymentMethod).statusOrder(StatusOrder.APPROVING).payment(payment).buyAt(LocalDateTime.now()).address(addressBuilder.toString()).build();
        managerOrderService.addOrder(order);
        OrderItem orderItem = OrderItem.builder().order(order).book(book).quantityBuy(quantityBuy).totalPrice(totalPrice).build();
        managerOrderItemService.addOrderItem(orderItem);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        httpSession.setAttribute("order",order);
        httpSession.setAttribute("orderItems",orderItems);

        if (paymentMethod == PaymentMethod.TRANSFER){
            String paymentURL = vnPayService.createPayment(request,payment,orderItems);
            return "redirect:"+paymentURL;
        }


        model.addAttribute("valueVoucherTransfer",idTransfer);
        model.addAttribute("valueVoucherDiscount",idDiscount);
        model.addAttribute("where",addressDelivery);
        model.addAttribute("bookBuy",book);
        model.addAttribute("paymentMethod",paymentMethod);
        model.addAttribute("payment",payment);


        return "page_browsing";
    }
    @GetMapping("homepage/checkout_buy_many_product")
    public String getCheckoutPaymentManyProduct(@RequestParam(value = "cartItemIds", required = false) List<Integer> cartItemIds, @RequestParam Map<String, String> quantityInCart, Model model, HttpServletRequest request){
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            model.addAttribute("error", "Bạn chưa chọn sản phẩm nào để thanh toán.");
            return "redirect:/homepage/move_cart";
        }
        User user = (User) request.getSession().getAttribute("loggedUser");
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<CartItem>cartItemList = new ArrayList<>();
        for (Integer id : cartItemIds) {
            CartItem cartItem = managerCartItemService.getCartItemById(id);

            // Lấy số lượng từ quantityInCart["quantityInCart[ID]"]
            String key = "quantityInCart[" + id + "]";
            int quantity = Integer.parseInt(quantityInCart.getOrDefault(key, "1"));

            cartItem.setQuantity(quantity);
            cartItemList.add(cartItem);

            BigDecimal itemTotal = cartItem.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(itemTotal);
        }
        List<Object[]> listVoucherTransfer = managerUserVoucherService.getAllVoucherTransfer(user.getUserId());
        List<Object[]> listVoucherDiscount = managerUserVoucherService.getAllVoucherDiscount(user.getUserId());
        model.addAttribute("listVoucherTransfer",listVoucherTransfer);
        model.addAttribute("listVoucherDiscount",listVoucherDiscount);
        model.addAttribute("cartItemList",cartItemList);
        model.addAttribute("moneyShip",0);
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("payment",totalPrice);
        return "buy_many_product";
    }

    @PostMapping("/homepage/cart/buy_many_product")
    public String paymentManyProduct(@RequestParam(value = "cartItemIds", required = false) List<Integer> cartItemIds,
                                     @RequestParam Map<String, String> quantityInCart,
                                     @RequestParam("where") String address,
                                     @RequestParam(value = "valueVoucherTransfer", required = false) Integer idTransfer,
                                     @RequestParam(value = "valueVoucherDiscount", required = false) Integer idDiscount,
                                     @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                                     HttpServletRequest request,
                                     HttpSession session,
                                     Model model) {

        User user = (User) request.getSession().getAttribute("loggedUser");

        // Lấy và xử lý địa chỉ
        String[] addressSplit = address.split("-");
        String addressShip = addressSplit[addressSplit.length - 1];
        ShipCostDocument shipCostDocument = searchShipCostService.getShipCostByCity(addressShip);
        String addressDelivery = shipCostDocument.getNameCity();
        BigDecimal shipCostValue = BigDecimal.valueOf(shipCostDocument.getCost());
        model.addAttribute("moneyShip",shipCostValue);

        // Lấy CartItem và tính tổng tiền
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Integer id : cartItemIds){
            CartItem cartItem = managerCartItemService.getCartItemById(id);
            String key = "quantityInCart[" + id + "]";
            int quantity = Integer.parseInt(quantityInCart.getOrDefault(key, "1"));

            BigDecimal itemTotal = cartItem.getBook().getPrice().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(itemTotal);
            OrderItem orderItem = OrderItem.builder().book(cartItem.getBook()).quantityBuy(quantity).totalPrice(itemTotal).build();
            orderItems.add(orderItem);
        }

        // Áp dụng voucher

        List<Object[]> listVoucherTransfer = managerUserVoucherService.getAllVoucherTransfer(user.getUserId());
        List<Object[]> listVoucherDiscount = managerUserVoucherService.getAllVoucherDiscount(user.getUserId());

        List<Voucher> voucherList = new ArrayList<>();
        for (Object[] voucher : listVoucherTransfer){
            Voucher voucherTransfer = (Voucher) voucher[0];
            if (voucherTransfer.getVoucherCode().equals(idTransfer)){
                voucherList.add(voucherTransfer);
            }
        }

        for (Object[] voucher : listVoucherDiscount){
            Voucher voucherDiscount = (Voucher) voucher[0];
            if (voucherDiscount.getVoucherCode().equals(idDiscount)){
                voucherList.add(voucherDiscount);
            }
        }

        BigDecimal payment = totalPrice.add(shipCostValue);
        for (Voucher voucher : voucherList) {
            if (voucher.getTypeVoucher().equals(TypeVoucher.TRANSPORT)) {
                BigDecimal discountAmount = shipCostValue.multiply(BigDecimal.valueOf(voucher.getValue())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                payment = payment.subtract(discountAmount);
            } else if (voucher.getTypeVoucher().equals(TypeVoucher.DISCOUNT)) {
                BigDecimal discountTransport = totalPrice.multiply(BigDecimal.valueOf(voucher.getValue())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                payment = payment.subtract(discountTransport);
            }
        }

        // Lưu Order
        ShipCost shipCost = managerShipCostService.getShipCostById(shipCostDocument.getShipCostId());
        Order order = Order.builder().user(user).totalPrice(totalPrice).shipCost(shipCost).voucherList(voucherList).paymentMethod(paymentMethod)
                .statusOrder(StatusOrder.APPROVING).payment(payment).buyAt(LocalDateTime.now()).address(addressDelivery).build();
        managerOrderService.addOrder(order);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
            managerOrderItemService.addOrderItem(item);
        }

        model.addAttribute("cartItemIds", cartItemIds);
        model.addAttribute("valueVoucherTransfer",idTransfer);
        model.addAttribute("valueVoucherDiscount",idDiscount);
        session.setAttribute("cartItemIds",cartItemIds);
        session.setAttribute("order", order);
        session.setAttribute("orderItems", orderItems);
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("where",address);


        // Thanh toán chuyển khoản
        if (paymentMethod == PaymentMethod.TRANSFER) {
            String url = vnPayService.createPayment(request, payment, orderItems);
            return "redirect:" + url;
        }

        model.addAttribute("payment", payment);
        model.addAttribute("address", address);
        model.addAttribute("paymentMethod", paymentMethod);
        // Xóa cart item đã mua
        for (Integer id : cartItemIds) {
            managerCartItemService.removeCartItem(id);
        }
        return "page_browsing";
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

    @GetMapping("/homepage/history_buy")
    public String historyBuyOfUser(Model model,@RequestParam(value = "valuePage",defaultValue = "0") int valuePage){
        Pageable pageable = PageRequest.of(valuePage,5);
        Page<Object[]> historyBuy = managerOrderService.getHistoryBuyProduct(pageable);
        Map<Integer, List<Object[]>> groupedOrders = historyBuy.stream()
                .collect(Collectors.groupingBy(order -> (Integer) order[0]));

        model.addAttribute("groupedOrders", groupedOrders);
        model.addAttribute("valuePage",valuePage);
        model.addAttribute("totalPage",historyBuy.getTotalPages());

        return "page_history_buy";
    }

    @PostMapping("/admin/manage_order/save")
    public String saveOrder(@RequestParam("valuePage") int valuePage,@RequestParam("orderId") List<String> listOrder, @RequestParam("handlerOrder") List<String> listHandlerOrder,@RequestParam("statusOrder") List<String> listStatusOrder,@RequestParam("filterOrder") String filterOrder){
        for (int i = 0; i < listOrder.size(); i++){
            Order order = managerOrderService.getOrderById(Integer.parseInt(listOrder.get(i)));
            if (listHandlerOrder.get(i) != null && !listHandlerOrder.get(i).isEmpty()) {
                order.setHandlerOrder(HandlerOrder.valueOf(listHandlerOrder.get(i)));
                order.setStatusOrder(StatusOrder.valueOf(listStatusOrder.get(i)));

            }else {
                order.setHandlerOrder(null);
            }
            managerOrderService.updateOrder(order);
        }

        return "redirect:/admin/manage_order/filter_order?filterOrder=" +  filterOrder
                + "&valuePage=" + valuePage;

    }
    @RequestMapping(value = "/admin/manage_order/filter_order", method = {RequestMethod.GET, RequestMethod.POST})
    public String filterOrder(@RequestParam("filterOrder") String filterOrder, @RequestParam(value = "valuePage",defaultValue = "0") int valuePage ,Model model){
        Pageable pageable = PageRequest.of(valuePage,10);
        Page<Object[]> pageOrder = null;
        if (filterOrder.equals("pending")){
            pageOrder = managerOrderService.getOrderNull(pageable);
        }else if (filterOrder.equals("accepted")){
            pageOrder = managerOrderService.getOrderAccept(pageable);
        }else if (filterOrder.equals("refused")){
            pageOrder = managerOrderService.getOrderRefuse(pageable);
        } else {
            pageOrder = managerOrderService.getInformationOrder(pageable);
        }


        model.addAttribute("valuePage",valuePage);
        model.addAttribute("totalPage", pageOrder.getTotalPages());
        model.addAttribute("listOrder",pageOrder);
        model.addAttribute("section","manage_order");
        model.addAttribute("filterOrder",filterOrder);
        return "admin_home";

    }

    @PostMapping("/admin/manage_order/filter_payment")
    public String filterPaymentByOrder(@RequestParam("filterMethod") String filterMethod, Model model,@RequestParam(value = "valuePage",defaultValue = "0") int valuePage){
        Pageable pageable = PageRequest.of(valuePage,10);
        if (filterMethod.equals("cash")){
            Page<Object[]> listOrderCash = managerOrderService.getOrderCash(pageable);
            model.addAttribute("listOrder",listOrderCash);
            model.addAttribute("totalPage", listOrderCash.getTotalPages());

        }else {
            Page<Object[]> listOrderTransfer = managerOrderService.getOrderTransfer(pageable);
            model.addAttribute("listOrder",listOrderTransfer);
            model.addAttribute("totalPage", listOrderTransfer.getTotalPages());

        }

        model.addAttribute("valuePage",valuePage);
        model.addAttribute("section","manage_order");
        model.addAttribute("filterMethod",filterMethod);
        return "admin_home";

    }
}
