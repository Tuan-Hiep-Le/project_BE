package com.example.project.entity;

import com.example.project.entity.enum_entity.Language;
import lombok.*;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BookDocument {
    @org.springframework.data.annotation.Id
    private Integer bookId;
    private String bookImage;
    private String nameBook;
    private Language language;
    private String nameAuthor;
    private String nameCategory;
    private String nameTopic;
    private BigDecimal price;
    private int quantity;
    private String bookDetail;


}

