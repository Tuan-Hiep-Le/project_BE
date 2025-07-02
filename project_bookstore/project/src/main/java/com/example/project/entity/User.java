package com.example.project.entity;

import com.example.project.entity.enum_entity.Role;
import com.example.project.entity.enum_entity.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer userId;
    @Column(name = "last_name",length = 50)
    private String lastName;
    @Column(name = "first_name",length = 50)
    private String firstName;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "phone_number",length = 10)
    private String phoneNumber;
    @Column(name = "password",length = 100)
    private String password;
    @Column(name = "avatar")
    private String avatar;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;



}
