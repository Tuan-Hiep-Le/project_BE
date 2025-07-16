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
    public ShipCostDocument getShipCostByCity(String nameCity) {
        try {
            String normalCity = normalizeCityName(nameCity).toLowerCase();

            SearchResponse<ShipCostDocument> response = elasticsearchClient.search(s -> s
                            .index("shipcost_document")
                            .query(q -> q.bool(b -> b.should(s1 -> s1.matchPhrase(mp -> mp.field("nameCity").query(normalCity)))
                                    .should(s2 -> s2.match(m -> m
                                            .field("nameCity")
                                            .query(normalCity)
                                            .fuzziness("1") // Cho phép sai 1 ký tự
                                            .minimumShouldMatch("100%")
                                    ))
                                    .minimumShouldMatch("1")
                            )),
                    ShipCostDocument.class
            );

            List<ShipCostDocument> resultList = response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .toList();

            if (resultList.isEmpty()) {
                throw new RuntimeException("Không tìm thấy phí vận chuyển cho: " + nameCity);
            }

            return resultList.get(0); // Trả về kết quả đầu tiên

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm phí vận chuyển với Elasticsearch", e);
        }
    }

    //Mapping ShipCostDocument và ShipCost
    public ShipCostDocument toshipCostDocument(ShipCost shipCost){

        return ShipCostDocument.builder()
                .shipCostId(shipCost.getShipCostId())
                .nameCity(shipCost.getNameCity())
                .cost(shipCost.getCost())
                .build();
    }

    //Dong bo hoa du lieu ShipCostDocument va ShipCost
    public void syncShipCost(){

        List<ShipCost> list = managerShipCostService.getAllShipCost();
        for (ShipCost shipCost  : list){
            ShipCostDocument shipCostDocument = toshipCostDocument(shipCost);
            try {
                elasticsearchClient.index(i -> i.index("shipcost_document").id(String.valueOf(shipCostDocument.getShipCostId())).document(shipCostDocument));
                System.out.println("Indexing city: " + shipCostDocument.getNameCity());

            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }

    }
    //Chuẩn hóa Tỉnh
    private String normalizeCityName(String input) {
        return input.toLowerCase()
                .replace("thành phố", "")
                .replace("tp.", "")
                .replace("tp", "")
                .replace("city", "")
                .replace("\"", "")
                .trim();
    }



}
