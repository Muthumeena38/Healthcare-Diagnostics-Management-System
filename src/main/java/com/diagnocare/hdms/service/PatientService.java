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
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ReportRepository reportRepository;

    public List<Test> getAvailableTests(){
        return testRepository.findAll();
    }

    public Booking bookTest(String patientEmail,Long testId,Booking bookingDetails){
        User patient=userRepository.findByEmail(patientEmail).orElseThrow(()-> new RuntimeException("Patient not found"));
        Test test=testRepository.findById(testId).orElseThrow(()-> new RuntimeException("Test not found"));

        Booking booking=new Booking();
        booking.setPatient(patient);
        booking.setTest(test);
        booking.setBookingDate(bookingDetails.getBookingDate());
        booking.setNotes(bookingDetails.getNotes());
        booking.setStatus(Booking.Status.BOOKED);
        booking.setCreatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public List<Booking> getMyBookings(String patientEmail){
        User patient=userRepository.findByEmail(patientEmail).orElseThrow(()-> new RuntimeException("Patient not found"));
        return bookingRepository.findByPatient(patient);
    }

    public List<Report> getMyReports(String patientEmail){
        User patient=userRepository.findByEmail(patientEmail).
                orElseThrow(()-> new RuntimeException("Patient not found"));
        return reportRepository.findByPatient(patient);
    }
}
