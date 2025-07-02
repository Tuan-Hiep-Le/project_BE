package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_history")
    private Integer IdHistoryLogin;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User  user;
    @Column(name = "login_time")
    private LocalDateTime loginTime;

}
