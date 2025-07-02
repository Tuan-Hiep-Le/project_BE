package com.example.project.entity;

import com.example.project.entity.enum_entity.HandlerOrder;
import com.example.project.entity.enum_entity.PaymentMethod;
import com.example.project.entity.enum_entity.StatusOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @Column(name = "total_price",precision = 10,scale = 2)
    private BigDecimal totalPrice;
    @OneToOne
    @JoinColumn(name = "shipcost_code")
    private ShipCost shipCost;
    @ManyToMany
    @JoinTable(
            name= "order_voucher",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "voucher_code")
    )
    private List<Voucher> voucherList;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_order")
    private StatusOrder statusOrder;
    @Enumerated(EnumType.STRING)
    @Column(name = "handle_order")
    private HandlerOrder handlerOrder;
    @Column(name = "payment", precision = 10,scale = 2)
    private BigDecimal payment;
}
