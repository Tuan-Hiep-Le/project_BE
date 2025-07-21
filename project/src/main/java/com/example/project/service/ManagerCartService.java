package com.example.project.service;

import com.example.project.entity.Cart;
import com.example.project.entity.CartItem;
import com.example.project.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ManagerCartService {
    //Thêm Cart vào database
    public Cart addCart(Cart cart);


    //Lấy ra Cart của người dùng
    public Cart getCartByUser(User user);
}
