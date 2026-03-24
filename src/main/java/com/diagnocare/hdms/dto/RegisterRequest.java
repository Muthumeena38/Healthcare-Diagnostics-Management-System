package com.diagnocare.hdms.dto;

import com.diagnocare.hdms.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private User.Role role;
    private String licenseNumber;
    private String specialization;
    private String phone;
}
