package com.project.repository;

import com.project.entity.Order;
import com.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerOrderRepository extends JpaRepository<Order,Integer> {
    //Lấy ra order người dùng mua sách gần nhất
    @Query(value = "SELECT o.*\n" +
            "FROM orders o\n" +
            "JOIN order_items oi ON o.order_id = oi.id_order\n" +
            "LEFT JOIN reviews r ON o.order_id = r.order_id AND r.id_book = oi.id_book\n" +
            "WHERE o.id_user = :userId AND oi.id_book =:idBook AND o.handle_order = 'ACCEPT' AND o.status_order = 'DELIVERED' \n" +
            "ORDER BY o.order_at DESC\n" +
            "LIMIT 1\n",nativeQuery = true)
    public Order getOrderBuyBookLates(@Param("userId")Integer userId, @Param("idBook")Integer idBook);

    //Lấy ra lịch sử mua hàng của người dùng
    @Query("SELECT o.orderId, oi.book, oi.quantityBuy, o.paymentMethod, o.statusOrder, o.handlerOrder, o.payment, o.buyAt, o.address\n" +
            "FROM Order o\n" +
            "JOIN o.orderItems oi ON o.orderId = oi.order.orderId\n" +
            "WHERE o.user = :user")
    public List<Object[]> findHistoryBuyProduct(@Param("user") User user);

}
