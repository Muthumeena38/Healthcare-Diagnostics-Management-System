package com.diagnocare.hdms.service;
import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.User;
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

    public List<Report> getAllReports(){
        return reportRepository.findAll();
    }

    public List<Report> getPatientReports(Long patientId){
        User patient=userRepository.findById(patientId)
                .orElseThrow(()-> new RuntimeException("Patient not found"));
        return reportRepository.findByPatient(patient);
    }

    public Report addRemark(Long reportId,String doctorEmail,String remark){
        Report report=reportRepository.findById(reportId)
                .orElseThrow(()-> new RuntimeException("Report not found"));
        User doctor=userRepository.findByEmail(doctorEmail)
                .orElseThrow(()-> new RuntimeException("Doctor not found"));
        report.setDoctor(doctor);
        report.setRemarks(remark);
        report.setDoctorRemarkDate(LocalDateTime.now());

        return reportRepository.save(report);
    }
}
