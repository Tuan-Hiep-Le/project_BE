package com.example.project.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.project.entity.ShipCost;
import com.example.project.entity.elastic.ShipCostDocument;
import com.example.project.service.SearchShipCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class SearchShipCostServiceImpl implements SearchShipCostService {
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private ManagerShipCostServiceImpl managerShipCostService;

    @Override
    public String getShipCostByCity(String nameCity) {
        try {
            SearchResponse<ShipCostDocument> shipCostDocumentSearchResponse = elasticsearchClient.search(s-> s.index("shipcost_document")
                    .query(q -> q.fuzzy(f -> f.field("nameCity").value(nameCity).fuzziness("1"))), ShipCostDocument.class);
            List<String> list = shipCostDocumentSearchResponse.hits().hits().stream().map(m -> m.id()).toList();
            if (list.isEmpty()){
                throw new RuntimeException("Không tìm thấy sản phẩm");
            }
            return list.get(0);

        }catch (Exception e){
            throw new RuntimeException("Lỗi khi dùng elasticSearch");
        }
    }
    //Mapping ShipCostDocument và ShipCost
    public ShipCostDocument toshipCostDocument(ShipCost shipCost){
        return ShipCostDocument.builder().shipCostId(shipCost.getShipCostId()).nameCity(shipCost.getNameCity()).cost(shipCost.getCost()).build();
    }

    //Dong bo hoa du lieu ShipCostDocument va ShipCost
    public void syncShipCost(){
        List<ShipCost> list = managerShipCostService.getAllShipCost();
        for (ShipCost shipCost  : list){
            ShipCostDocument shipCostDocument = toshipCostDocument(shipCost);
            try {
                elasticsearchClient.index(i -> i.index("shipcost_document").id(String.valueOf(shipCostDocument.getShipCostId())).document(shipCostDocument));

            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }

    }


}
