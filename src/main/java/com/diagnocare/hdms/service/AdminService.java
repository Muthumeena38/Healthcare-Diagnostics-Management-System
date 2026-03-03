package com.diagnocare.hdms.service;

import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.Test;
import com.diagnocare.hdms.model.User;
import com.diagnocare.hdms.model.Booking;
import com.diagnocare.hdms.repository.UserRepository;
import com.diagnocare.hdms.repository.BookingRepository;
import com.diagnocare.hdms.repository.ReportRepository;
import com.diagnocare.hdms.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    private final String UPLOAD_DIR="uploads/reports/";

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Test createTest(Test test){
        test.setCreatedAt(LocalDateTime.now());
        return testRepository.save(test);
    }

    public List<Test> getAllTests(){
        return testRepository.findAll();
    }

    public void deleteTest(Long id){
        testRepository.deleteById(id);
    }

    public Report uploadReport(Long patientId,Long testId, Long adminId,
                               MultipartFile file,String remarks) throws IOException{
        Path uploadPath=Paths.get(UPLOAD_DIR);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        String fileName=UUID.randomUUID()+"_"+file.getOriginalFilename();
        Path filePath=uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(),filePath);

        User patient=userRepository.findById(patientId).orElseThrow(() ->new RuntimeException("Patient not found"));
        Test test=testRepository.findById(testId).orElseThrow(() ->new RuntimeException("Test not found"));
        User admin=userRepository.findById(adminId).orElseThrow(() ->new RuntimeException("Admin not found"));

        Report report=new Report();
        report.setPatient(patient);
        report.setTest(test);
        report.setUploadedBy(admin);
        report.setFileName(file.getOriginalFilename());
        report.setFilePath(filePath.toString());
        report.setRemarks(remarks);
        report.setUploadDate(LocalDateTime.now());

        return reportRepository.save(report);
    }

    public List<Report> getAllReports(){
        return reportRepository.findAll();
    }

    public void deleteReport(Long id){
        reportRepository.deleteById(id);
    }

    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Booking updateBookingStatus(Long id, Booking.Status status){
        Booking booking=bookingRepository.findById(id).orElseThrow(() ->new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

}
