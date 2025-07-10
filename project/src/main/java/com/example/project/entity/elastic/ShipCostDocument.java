package com.example.project.entity.elastic;

import lombok.*;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShipCostDocument {
    @Id
    private int shipCostId;
    private String nameCity;
    private int cost;
}
