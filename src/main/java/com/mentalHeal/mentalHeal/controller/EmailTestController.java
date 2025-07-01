package com.mentalHeal.mentalHeal.controller;

import com.mentalHeal.mentalHeal.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping("/test-email")
    public String sendTestEmail() {
        System.out.println("✅ Test email endpoint called");
        emailService.sendSimpleMessage(
                "bandikaushikreddy@gmail.com",  // Replace with your real email to test
                "Test Email from MentalHeal",
                "This is a test email to verify the mail setup."
        );
        return "Email sent successfully!";
    }
}

