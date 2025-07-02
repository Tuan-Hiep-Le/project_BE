package com.example.project.entity;

import com.example.project.entity.Book;
import com.example.project.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart")
    private Integer cartId;
    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;


}
