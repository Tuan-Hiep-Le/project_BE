package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ship_costs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShipCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipcost_code")
    private Integer shipCostId;
    @Column(name = "name_city",length = 50)
    private String nameCity;
    @Column(name = "cost")
    private int cost;
}
