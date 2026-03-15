package com.diagnocare.hdms.controller;

import com.diagnocare.hdms.model.Booking;
import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.Test;
import com.diagnocare.hdms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import com.diagnocare.hdms.model.User;

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

    // Get patient profile with Health ID
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestParam String email) {
        return ResponseEntity.ok(
                patientService.getProfile(email)
        );
    }

    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, @RequestParam String email) {
        try {
            return ResponseEntity.ok(patientService.cancelBooking(id, email));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}