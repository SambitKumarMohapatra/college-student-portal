package com.college.college.portal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "student")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roll_number",unique = true,length = 20)
    private String rollNumber;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(unique = true,nullable = false,length=100)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(name = "password_hash",nullable = false,length=225)
    private String passwordHash;

    @Column(length = 50)
    private String branch; //CSE,ECE,ME,CE,EE

    @Column
    private Integer semester; //1-8

    @Column(name = "enrollment_year")
    private Integer enrollmentYear;

    @Column
    private LocalDate dob;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 10)
    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.STUDENT;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
