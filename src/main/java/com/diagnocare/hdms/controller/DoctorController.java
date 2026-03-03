package com.diagnocare.hdms.controller;

import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Get all reports
    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(doctorService.getAllReports());
    }

    // Get specific patient reports
    @GetMapping("/reports/patient/{patientId}")
    public ResponseEntity<List<Report>> getPatientReports(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(doctorService.getPatientReports(patientId));
    }

    // Add remark to report
    @PutMapping("/reports/{reportId}/remark")
    public ResponseEntity<Report> addRemark(
            @PathVariable Long reportId,
            @RequestParam String remark,
            @RequestParam String email) {
        return ResponseEntity.ok(
                doctorService.addRemark(reportId, email, remark)
        );
    }
}