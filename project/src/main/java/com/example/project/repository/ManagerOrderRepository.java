package com.example.project.repository;

import com.example.project.entity.Order;
import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagerOrderRepository extends JpaRepository<Order,Integer> {
    //Lấy ra order người dùng mua sách gần nhất
    @Query(value = "SELECT o.*\n" +
            "FROM orders o\n" +
            "JOIN order_items oi ON o.order_id = oi.id_order\n" +
            "LEFT JOIN reviews r ON o.order_id = r.order_id AND r.id_book = oi.id_book\n" +
            "WHERE o.id_user = :userId AND oi.id_book =:idBook\n" +
            "ORDER BY o.order_at DESC\n" +
            "LIMIT 1\n",nativeQuery = true)
    public Order getOrderBuyBookLates(@Param("userId")Integer userId, @Param("idBook")Integer idBook);


}
