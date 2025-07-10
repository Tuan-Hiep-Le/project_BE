package com.example.project.service;

import com.example.project.entity.ShipCost;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ManagerShipCostService {
    //Lay tat ca shipCost
    public List<ShipCost> getAllShipCost();

    //Lay ShipCost by Id
    public ShipCost getShipCostById(Integer id);
}
