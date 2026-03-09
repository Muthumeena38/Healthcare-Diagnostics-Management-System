package com.diagnocare.hdms.service;

import com.diagnocare.hdms.model.AuditLog;
import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.User;
import com.diagnocare.hdms.repository.AuditLogRepository;
import com.diagnocare.hdms.repository.ReportRepository;
import com.diagnocare.hdms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> getPatientReports(String healthId, String doctorEmail) {
        User patient = userRepository.findByHealthId(healthId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        AuditLog log = new AuditLog();
        log.setDoctor(doctor);
        log.setPatient(patient);
        log.setViewedAt(LocalDateTime.now());
        auditLogRepository.save(log);

        return reportRepository.findByPatient(patient);
    }

    public Report addRemark(Long reportId, String doctorEmail, String remark) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        User doctor = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        report.setDoctor(doctor);
        report.setDoctorRemark(remark);
        report.setDoctorRemarkDate(LocalDateTime.now());
        return reportRepository.save(report);
    }
}
