package com.mentalHeal.mentalHeal.controller;

import com.mentalHeal.mentalHeal.DTO.OnboardingRequest;
import com.mentalHeal.mentalHeal.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    @Autowired
    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping
    public ResponseEntity<String> submitOnboarding(
            @RequestBody OnboardingRequest request,
            Principal principal) {
        System.out.println("Received onboarding POST request");
        System.out.println("Principal: " + (principal != null ? principal.getName() : "null"));
        System.out.println("Request payload: " + request);

        // principal.getName() should return the logged-in user's email or username
        String email = principal.getName();

        onboardingService.updateOnboardingDetails(email, request);

        return ResponseEntity.ok("Onboarding details saved successfully");
    }
}
