package com.example.bankapp.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotification(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

message.setFrom(System.getenv("MAIL_USERNAME") != null ? System.getenv("MAIL_USERNAME") : "noreply@bankapp.com");

            mailSender.send(message);
            System.out.println("EMAIL SENT to " + toEmail);
        } catch (Exception e) {
            System.out.println("FAILED TO SEND EMAIL: " + e.getMessage());
        }
    }


    public void sendLowBalanceAlert(String toEmail, String name, double balance) {
        String subject = "⚠️ Low Balance Alert";
        String body = "Dear " + name + ",\n\nYour account balance is low.\nCurrent Balance: $" + balance + "\n\nPlease deposit funds soon.";

        sendNotification(toEmail, subject, body);
    }
}