package com.diagnocare.hdms.controller;

import com.diagnocare.hdms.model.Booking;
import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.Test;
import com.diagnocare.hdms.model.User;
import com.diagnocare.hdms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins="*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private com.diagnocare.hdms.security.JwtUtil jwtUtil;

    @Autowired
    private com.diagnocare.hdms.repository.UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/tests")
    public ResponseEntity<Test> createTest(@RequestBody Test test){
        return ResponseEntity.ok(adminService.createTest(test));
    }

    @GetMapping("/tests")
    public ResponseEntity<List<Test>> getAllTests(){
        return ResponseEntity.ok(adminService.getAllTests());
    }

    @DeleteMapping("/tests/{id}")
    public ResponseEntity<String> deleteTest(@PathVariable Long id){
        adminService.deleteTest(id);
        return ResponseEntity.ok("Test deleted Successfully");
    }

    @PostMapping("/reports/upload")

    public ResponseEntity<Report> uploadReport(
            @RequestParam("patientId") Long patientId,
            @RequestParam("testId") Long testId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value="remarks", required=false) String remarks,
            @RequestHeader("Authorization") String authHeader) throws IOException {

        String token = authHeader.substring(7);
        String adminEmail = jwtUtil.getEmailFromToken(token);
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return ResponseEntity.ok(
                adminService.uploadReport(patientId, testId, admin.getId(), file, remarks)
        );
    }

    // Download/View PDF endpoint
    @GetMapping("/reports/{id}/view")
    public ResponseEntity<byte[]> viewReport(@PathVariable Long id) {
        Report report = adminService.getReportById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + report.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(report.getFileContent());
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports(){
        return ResponseEntity.ok(adminService.getAllReports());
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable Long id){
        adminService.deleteReport(id);
        return ResponseEntity.ok("Report deleted Successfully");
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings(){
        return ResponseEntity.ok(adminService.getAllBookings());
    }

    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<Booking> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam Booking.Status status){
        return ResponseEntity.ok(adminService.updateBookingStatus(id, status));
    }

    @GetMapping("/doctors/pending")
    public ResponseEntity<List<User>> getPendingDoctors() {
        return ResponseEntity.ok(adminService.getPendingDoctors());
    }

    @PutMapping("/doctors/{id}/status")
    public ResponseEntity<User> updateDoctorStatus(
            @PathVariable Long id,
            @RequestParam User.Status status) {
        return ResponseEntity.ok(adminService.updateDoctorStatus(id, status));
    }
}