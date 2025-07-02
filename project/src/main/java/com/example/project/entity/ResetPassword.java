package com.example.project.entity;

import com.example.project.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "reset_passwords")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rest_password")
    private Integer idResetPassword;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @Column(name = "reset_password_toke",length = 255)
    private String resetPasswordToken;
    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;
}
