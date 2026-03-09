package com.diagnocare.hdms.service;

import com.diagnocare.hdms.dto.AuthResponse;
import com.diagnocare.hdms.dto.LoginRequest;
import com.diagnocare.hdms.dto.RegisterRequest;
import com.diagnocare.hdms.model.User;
import com.diagnocare.hdms.repository.UserRepository;
import com.diagnocare.hdms.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        // Doctor needs admin approval
        if (request.getRole() == User.Role.DOCTOR) {
            user.setStatus(User.Status.PENDING);
            user.setLicenseNumber(request.getLicenseNumber());
            user.setSpecialization(request.getSpecialization());
        }else {
        user.setStatus(User.Status.ACTIVE);
        if (request.getRole() == User.Role.PATIENT) {
            user.setHealthId(generateHealthId());
        }
    }

        userRepository.save(user);

        // Doctor cannot login until approved
        if (request.getRole() == User.Role.DOCTOR) {
            return new AuthResponse(null, user.getRole().name(), user.getName(), "PENDING");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getName(), "ACTIVE");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Block pending/rejected doctors
        if (user.getRole() == User.Role.DOCTOR && user.getStatus() == User.Status.PENDING) {
            throw new RuntimeException("Your account is pending approval");
        }

        if (user.getStatus() == User.Status.SUSPENDED) {
            throw new RuntimeException("Account access restricted. Please contact support at support@diagnocare.com");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getName(),
                user.getStatus() != null ? user.getStatus().name() : "ACTIVE");
    }

    private String generateHealthId() {
        int year = LocalDateTime.now().getYear();
        long count = userRepository.countByRole(User.Role.PATIENT) + 1;
        return String.format("DC-%d-%05d", year, count);
    }
}
