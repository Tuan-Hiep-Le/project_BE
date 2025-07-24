package com.project.repository;

import com.project.entity.ShipCost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerShipCostRepository extends JpaRepository<ShipCost,Integer> {
    public ShipCost findByShipCostId(Integer id);
}
