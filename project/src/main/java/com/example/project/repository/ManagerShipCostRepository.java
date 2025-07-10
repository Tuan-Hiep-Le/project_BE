package com.example.project.repository;

import com.example.project.entity.ShipCost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerShipCostRepository extends JpaRepository<ShipCost,Integer> {
    public ShipCost findByShipCostId(Integer id);
}
