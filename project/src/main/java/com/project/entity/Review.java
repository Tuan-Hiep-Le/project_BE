package com.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Integer idReview;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_book")
    private Book  book;
    @Column(name = "star_rate")
    private int starRate;
    @Column(name = "comment",length = 255)
    private String comment;
    @Column(name = "reviewAt")
    private LocalDateTime reviewAt;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
