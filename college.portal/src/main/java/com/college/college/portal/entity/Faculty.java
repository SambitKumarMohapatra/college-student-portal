package com.college.college.portal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "faculty_personal")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(unique = true,nullable = false,length = 100)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(name = "password_hash",nullable = false,length = 255)
    private String passwordHash;

    @Column(length=50)
    private String designation;

    @Column(length=50)
    private String department;

    @Column(length=100)
    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role=Role.FACULTY;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

}
