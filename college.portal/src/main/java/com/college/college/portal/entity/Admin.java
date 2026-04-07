package com.college.college.portal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length=100)
    private String name;

    @Column(unique = true,nullable = false,length=100)
    private String email;

    @Column(name = "password_hash",nullable = false,length=225)
    private String passwordHash;

    @Column(length=50)
    private String adminRole;

    @Column(length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role=Role.ADMIN;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;
}
