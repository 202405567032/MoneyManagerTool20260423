package com.personalfinance.accountingsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;   // bcrypt加密存储

    @Column(unique = true)
    private String email;

    @Column(name = "register_time")
    private LocalDateTime registerTime;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerTime = LocalDateTime.now();
    }
}