package com.project.service.impl;

import com.project.entity.ShipCost;
import com.project.repository.ManagerShipCostRepository;
import com.project.service.ManagerShipCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ManagerShipCostServiceImpl implements ManagerShipCostService {
    @Autowired
    private ManagerShipCostRepository managerShipCostRepository;
    @Override
    public List<ShipCost> getAllShipCost() {
        return managerShipCostRepository.findAll();
    }

    @Override
    public ShipCost getShipCostById(Integer id) {
        return managerShipCostRepository.findByShipCostId(id);
    }
}
