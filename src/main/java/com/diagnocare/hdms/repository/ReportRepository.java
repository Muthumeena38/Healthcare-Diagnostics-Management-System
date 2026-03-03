package com.diagnocare.hdms.repository;

import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPatient(User patient);
    List<Report> findByDoctor(User doctor);
    List<Report> findByUploadedBy(User uploadedBy);
}
