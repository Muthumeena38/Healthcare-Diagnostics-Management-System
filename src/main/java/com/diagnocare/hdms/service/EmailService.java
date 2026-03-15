package com.diagnocare.hdms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReportNotification(String toEmail, String patientName, String testName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Report is Ready - DiagnoCare");
        message.setText(
                "Dear " + patientName + ",\n\n" +
                        "Your report for " + testName + " has been uploaded successfully.\n\n" +
                        "Login to DiagnoCare to view your report.\n\n" +
                        "Visit: https://hdms-j63x.onrender.com\n\n" +
                        "Regards,\nDiagnoCare Team"
        );
        mailSender.send(message);
    }

    public void sendForgotPasswordEmail(String toEmail, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset Your Password - DiagnoCare");
        message.setText(
                "Hello,\n\n" +
                        "Click the link below to reset your password:\n\n" +
                        "https://hdms-j63x.onrender.com/reset-password.html?token=" + resetToken + "\n\n" +
                        "This link expires in 30 minutes.\n\n" +
                        "If you didn't request this, ignore this email.\n\n" +
                        "Regards,\nDiagnoCare Team"
        );
        mailSender.send(message);
    }
}