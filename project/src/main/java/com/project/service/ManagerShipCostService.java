package com.project.service;

import com.project.entity.ShipCost;

import java.util.List;


public interface ManagerShipCostService {
    //Lay tat ca shipCost
    public List<ShipCost> getAllShipCost();

    //Lay ShipCost by Id
    public ShipCost getShipCostById(Integer id);
}
