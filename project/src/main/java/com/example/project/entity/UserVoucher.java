package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Table(name = "users_voucher")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_voucher")
    private Integer idUserVoucher;
    @OneToMany
    @JoinColumn(name = "id_user")
    private User user;
    @OneToMany
    @JoinColumn(name = "voucher_code")
    private Voucher voucher;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "quantity_user")
    private int quantityUsed;
    @Column(name = "date_received")
    private LocalDate dateReceived;
}
