package com.diagnocare.hdms.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.diagnocare.hdms.model.Booking;
import com.diagnocare.hdms.repository.BookingRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/create-order/{bookingId}")
    public ResponseEntity<?> createOrder(@PathVariable Long bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            double amount = booking.getTest().getPrice();

            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            JSONObject options = new JSONObject();
            options.put("amount", (int)(amount * 100)); // paise
            options.put("currency", "INR");
            options.put("receipt", "booking_" + bookingId);

            Order order = client.orders.create(options);

            return ResponseEntity.ok(Map.of(
                    "orderId", order.get("id"),
                    "amount", (int)(amount * 100),
                    "currency", "INR",
                    "keyId", keyId,
                    "bookingId", bookingId,
                    "testName", booking.getTest().getTestName(),
                    "patientName", booking.getPatient().getName(),
                    "patientEmail", booking.getPatient().getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/verify/{bookingId}")
    public ResponseEntity<?> verifyPayment(
            @PathVariable Long bookingId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpaySignature) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            booking.setStatus(Booking.Status.PAID);
            bookingRepository.save(booking);
            return ResponseEntity.ok(Map.of("message", "Payment successful!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}