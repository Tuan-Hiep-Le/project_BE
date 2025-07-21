package com.example.project.service;

import com.example.project.entity.Book;
import com.example.project.entity.CartItem;
import java.util.List;


public interface ManagerCartItemService {
    //Thêm CartItem
    public CartItem addCartItem(CartItem cartItem);

    //Lấy ra các sản phẩm trong giỏ hàng
    public List<CartItem> getAllCTByUserId(Integer id);

    //.Lấy ra CartItem bằng Book
    public CartItem getCartItemByBook(Book book);

    //Xóa sản phẩm ra khỏi giỏ hàng
    public void removeCartItem(Integer cartItemId);

}
