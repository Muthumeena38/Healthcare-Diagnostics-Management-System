package com.diagnocare.hdms.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "report_chunks")
public class ReportChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(columnDefinition = "TEXT")
    private String chunkText;

    private Integer chunkIndex;
}
