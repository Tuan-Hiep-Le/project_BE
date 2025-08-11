package com.project.controller;

import com.project.entity.Book;
import com.project.entity.Cart;
import com.project.entity.CartItem;
import com.project.entity.User;
import com.project.service.impl.ManagerBookServiceImpl;
import com.project.service.impl.ManagerCartItemServiceImpl;
import com.project.service.impl.ManagerCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ManagerCartItemController {
    @Autowired
    private ManagerCartItemServiceImpl managerCartItemService;
    @Autowired
    private ManagerBookServiceImpl managerBookService;
    @Autowired
    private ManagerCartServiceImpl managerCartService;

    //Thêm vào giỏ hàng
    @PostMapping("/homepage/add_cart_item")
    public String addCartItemInCart(@RequestParam("bookBuyId") Integer id, Model model){
        model.addAttribute("bookBuyId",id);
        Book book = managerBookService.getBookById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal() ;
        Cart cart = managerCartService.getCartByUser(user);
        if (cart == null) {
            cart = Cart.builder().user(user).build();
            managerCartService.addCart(cart);
        }

        LocalDateTime timeAdd = LocalDateTime.now();
        List<CartItem> cartItemList = managerCartItemService.getAllCTByUserId(user.getUserId());
        List<Book> listBook = new ArrayList<>();
        for (CartItem cartItem : cartItemList){
            listBook.add(cartItem.getBook());
        }
        if(listBook.contains(book)){
            CartItem cartItem = managerCartItemService.getCartItemByBook(book);
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            managerCartItemService.addCartItem(cartItem);
        } else {
            CartItem cartItem = CartItem.builder().book(book).cart(cart).quantity(1).timeAdd(timeAdd).build();
            managerCartItemService.addCartItem(cartItem);
        }
        return "redirect:/homepage";
    }


}
