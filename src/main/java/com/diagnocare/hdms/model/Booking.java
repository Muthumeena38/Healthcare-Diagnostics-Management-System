package com.diagnocare.hdms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.BOOKED;

    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum Status { PENDING, CONFIRMED, COMPLETED, BOOKED, CANCELLED, PAID }
}
