package com.diagnocare.hdms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "health_id", unique = true)
    private String healthId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    public enum Role {
        ADMIN, PATIENT, DOCTOR
    }

    public enum Status {
        ACTIVE, PENDING, APPROVED, REJECTED, SUSPENDED
    }
}
