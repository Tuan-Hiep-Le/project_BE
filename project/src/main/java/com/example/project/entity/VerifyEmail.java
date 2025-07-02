package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "verify_emails")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class VerifyEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veri_email")
    private Integer idVerifyEmail;
    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;
    @Column(name = "verified_email")
    private Boolean isVerifiedEmail;
    @Column(name = "verification_token_email", length = 255)
    private String verificationTokenEmail;
    @Column(name = "token_lifetime")
    private LocalDateTime tokenLifeTime;
}
