

package com.diagnocare.hdms.controller;

import com.diagnocare.hdms.model.Booking;
import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.Test;
import com.diagnocare.hdms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Get all available tests
    @GetMapping("/tests")
    public ResponseEntity<List<Test>> getAvailableTests() {
        return ResponseEntity.ok(patientService.getAvailableTests());
    }

    // Book a test
    @PostMapping("/book-test/{testId}")
    public ResponseEntity<Booking> bookTest(
            @PathVariable Long testId,
            @RequestBody Booking bookingDetails,
            @RequestParam String email) {
        return ResponseEntity.ok(
                patientService.bookTest(email, testId, bookingDetails)
        );
    }

    // Get my bookings
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getMyBookings(
            @RequestParam String email) {
        return ResponseEntity.ok(
                patientService.getMyBookings(email)
        );
    }

    // Get my reports
    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getMyReports(
            @RequestParam String email) {
        return ResponseEntity.ok(
                patientService.getMyReports(email)
        );
    }
}