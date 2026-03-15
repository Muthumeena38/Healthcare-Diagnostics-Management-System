package com.diagnocare.hdms.service;

import com.diagnocare.hdms.model.Booking;
import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.Test;
import com.diagnocare.hdms.model.User;
import com.diagnocare.hdms.repository.BookingRepository;
import com.diagnocare.hdms.repository.ReportRepository;
import com.diagnocare.hdms.repository.TestRepository;
import com.diagnocare.hdms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.diagnocare.hdms.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private EmailService emailService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Test createTest(Test test) {
        test.setCreatedAt(LocalDateTime.now());
        return testRepository.save(test);
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }

    public Report uploadReport(String healthId, Long testId, Long adminId,
                               MultipartFile file, String remarks) throws IOException {

        User patient = userRepository.findByHealthId(healthId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Report report = new Report();
        report.setPatient(patient);
        report.setTest(test);
        report.setUploadedBy(admin);
        report.setFileName(file.getOriginalFilename());
        report.setFileContent(file.getBytes());
        report.setRemarks(remarks);
        report.setUploadDate(LocalDateTime.now());

        Report saved = reportRepository.save(report);
        try {
            emailService.sendReportNotification(
                    patient.getEmail(),
                    patient.getName(),
                    test.getTestName()
            );
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
        return saved;
    }



    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking updateBookingStatus(Long id, Booking.Status status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    public List<User> getPendingDoctors() {
        return userRepository.findByRoleAndStatus(User.Role.DOCTOR, User.Status.PENDING);
    }

    public User updateDoctorStatus(Long doctorId, User.Status status) {
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setStatus(status);
        return userRepository.save(doctor);
    }

}