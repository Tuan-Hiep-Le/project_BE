package com.project.controller;

import com.project.entity.CartItem;
import com.project.entity.User;
import com.project.service.impl.ManagerCartItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ManagerCartController {
    @Autowired
    private ManagerCartItemServiceImpl cartItemService;
    @GetMapping("/home_after_user_login/move_cart")
    public String moveToCart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user  =  (User) authentication.getPrincipal();
        List<CartItem> cartItemList = cartItemService.getAllCTByUserId(user.getUserId());
        model.addAttribute("listCartItems",cartItemList);
        model.addAttribute("totalPrice",0);
        return "cart_page";
    }

    @PostMapping("/home_after_user_login/move_cart/remove_out_cart")
    public String removeCartItem(@RequestParam("itemId") Integer cartItemId, Model model){
        cartItemService.removeCartItem(cartItemId);
        model.addAttribute("itemId",cartItemId);
        return "redirect:/home_after_user_login/move_cart";

    }

}
