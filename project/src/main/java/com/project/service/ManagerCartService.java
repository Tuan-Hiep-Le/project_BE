package com.project.service;

import com.project.entity.Cart;
import com.project.entity.User;


public interface ManagerCartService {
    //Thêm Cart vào database
    public Cart addCart(Cart cart);


    //Lấy ra Cart của người dùng
    public Cart getCartByUser(User user);
}
