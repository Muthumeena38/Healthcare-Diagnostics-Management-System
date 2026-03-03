package com.diagnocare.hdms.controller;

import com.diagnocare.hdms.model.Booking;
import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.Test;
import com.diagnocare.hdms.model.User;
import com.diagnocare.hdms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam(value="remarks",required=false) String remarks,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException{

        User admin=userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Admin not found"));
        return ResponseEntity.ok(
                adminService.uploadReport(patientId,testId,admin.getId(),file,remarks)

        );
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports(){
        return ResponseEntity.ok(adminService.getAllReports());
    }

    @DeleteMapping("/reports/id")
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
            @RequestParam Booking.Status status
    ){
        return ResponseEntity.ok(adminService.updateBookingStatus(id,status));
    }
}
