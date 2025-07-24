package com.project.service;
import com.project.entity.elastic.ShipCostDocument;

public interface SearchShipCostService {
    public ShipCostDocument getShipCostByCity(String nameCity);
}
