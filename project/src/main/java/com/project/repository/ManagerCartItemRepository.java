package com.project.repository;

import com.project.entity.Book;
import com.project.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerCartItemRepository extends JpaRepository<CartItem,Integer> {
    //Lấy ra các sản phẩm trong giỏ hàng
    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE c.user.userId = :userId")
    public List<CartItem> listCartItem(@Param("userId") Integer userId);

    public CartItem findByBook(Book book);

    public void deleteById(Integer cartItemId);
}
