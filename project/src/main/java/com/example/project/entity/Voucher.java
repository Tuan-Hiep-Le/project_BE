package com.example.project.entity;

import com.example.project.entity.enum_entity.TypeVoucher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "vouchers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_code")
    private Integer voucherCode;
    @Column(name = "name_voucher",length = 50)
    private String nameVoucher;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")

    private TypeVoucher typeVoucher;
    @Column
    private int value;
    @Column(name = "start_time")
    private LocalDate startTime;
    @Column(name = "end_time")
    private LocalDate endTime;
    @Column(name = "total_quantity")
    private int totalQuantity;
    @Column(name = "current_quantity")
    private int currentQuantity;
}
