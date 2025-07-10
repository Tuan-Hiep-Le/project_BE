package com.example.project.service.impl;

import com.example.project.entity.ShipCost;
import com.example.project.repository.ManagerShipCostRepository;
import com.example.project.service.ManagerShipCostService;
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
