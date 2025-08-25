package com.project.service;

import com.project.entity.Book;
import com.project.entity.Cart;
import com.project.entity.CartItem;
import java.util.List;


public interface ManagerCartItemService {
    //Thêm CartItem
    public CartItem addCartItem(CartItem cartItem);

    //Lấy ra các sản phẩm trong giỏ hàng
    public List<CartItem> getAllCTByUserId(Integer id);

    //.Lấy ra CartItem bằng Book
    public CartItem getCartItemByBookAndCart(Book book, Cart cart);

    //Xóa sản phẩm ra khỏi giỏ hàng
    public void removeCartItem(Integer cartItemId);

    //Lấy ra CartItem bằng Id
    public CartItem getCartItemById(Integer cartItemId);

}
