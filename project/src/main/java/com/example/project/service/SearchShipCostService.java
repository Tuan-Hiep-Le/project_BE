package com.example.project.service;
import com.example.project.entity.elastic.ShipCostDocument;
import org.springframework.stereotype.Service;

public interface SearchShipCostService {
    public ShipCostDocument getShipCostByCity(String nameCity);
}
