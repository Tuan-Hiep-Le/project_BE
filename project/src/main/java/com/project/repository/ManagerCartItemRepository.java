package com.project.repository;

import com.project.entity.Book;
import com.project.entity.Cart;
import com.project.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerCartItemRepository extends JpaRepository<CartItem,Integer> {
    //Lấy ra các sản phẩm trong giỏ hàng
//    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE c.user.userId = :userId")
//    public List<CartItem> listCartItem(@Param("userId") Integer userId);
    @Query("SELECT ci FROM CartItem ci " +
            "JOIN FETCH ci.book " +
            "JOIN FETCH ci.cart c " +
            "JOIN FETCH c.user u " +
            "WHERE u.userId = :userId")
    List<CartItem> listCartItem(@Param("userId") Integer userId);


    public CartItem findByBookAndCart(Book book, Cart cart);

    public void deleteById(Integer cartItemId);


}
