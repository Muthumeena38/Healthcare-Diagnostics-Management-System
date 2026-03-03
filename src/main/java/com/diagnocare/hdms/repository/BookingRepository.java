package com.diagnocare.hdms.repository;

import com.diagnocare.hdms.model.Booking;
import com.diagnocare.hdms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPatient(User patient);
    List<Booking> findByStatus(Booking.Status status);
}
