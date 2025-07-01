package com.mentalHeal.mentalHeal.controller;

import com.mentalHeal.mentalHeal.DTO.LoginRequest;
import com.mentalHeal.mentalHeal.model.User;

import com.mentalHeal.mentalHeal.repository.UserRepository;
import com.mentalHeal.mentalHeal.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    // Registration endpoint
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("onboardingComplete", user.isOnboardingComplete());
        response.put("token", token);
        response.put("email", request.getEmail());
        response.put("name", user.getName());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/complete-onboarding")
    public ResponseEntity<?> completeOnboarding(Principal principal) {


        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

        try {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setOnboardingComplete(true);
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Onboarding marked as complete");
            response.put("onboardingComplete", String.valueOf(user.isOnboardingComplete()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
