package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_item")
    private Integer idOrderItem;
    @ManyToOne
    @JoinColumn(name = "id_order")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "id_book")
    private Book book;
    @Column(name = "quantity_buy")
    private int quantityBuy;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
}
