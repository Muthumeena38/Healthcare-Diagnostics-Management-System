package com.diagnocare.hdms.repository;

import com.diagnocare.hdms.model.ReportChunk;
import com.diagnocare.hdms.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportChunkRepository extends JpaRepository<ReportChunk, Long> {
    List<ReportChunk> findByReport(Report report);
    List<ReportChunk> findByReport_Patient_Id(Long patientId);
}
