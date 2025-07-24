package com.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart_item")
    private Integer cartItemId;
    @OneToOne
    @JoinColumn(name = "id_book")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "id_cart")
    private Cart cart;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "is_selected")
    private Boolean isSelected;
    @Column(name = "time_add")
    private LocalDateTime timeAdd;
}
