package com.diagnocare.hdms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @JsonIgnore
    @Lob
    @Column(name = "file_content", columnDefinition = "LONGBLOB")
    private byte[] fileContent;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(name = "doctor_remark")
    private String doctorRemark;

    @Column(name = "doctor_remark_date")
    private LocalDateTime doctorRemarkDate;
}