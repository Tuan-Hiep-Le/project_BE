package com.example.project.entity;

import com.example.project.entity.enum_entity.Language;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "index_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    @org.springframework.data.annotation.Id
    private Integer bookId;
    @Column(name = "img_book")
    private String bookImage;
    @Column(name = "name_book",length = 100)
    @Field(name = "name_book", type = FieldType.Text, analyzer = "vietnamese", searchAnalyzer = "vietnamese")
    private String nameBook;
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;
    @Column(name = "author",length = 50)
    @Field(type = FieldType.Text, analyzer = "vietnamese", searchAnalyzer = "vietnamese")
    private String author;
    @Column(name = "category",length = 50)
    @Field(type = FieldType.Text, analyzer = "vietnamese", searchAnalyzer = "vietnamese")
    private String category;
    @Column(name = "topic",length = 50)
    private String topic;
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name = "quantity")
    private int quantity;
}