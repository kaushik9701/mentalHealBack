package com.mentalHeal.mentalHeal.service;

import com.mentalHeal.mentalHeal.DTO.OnboardingRequest;
import com.mentalHeal.mentalHeal.model.User;
import com.mentalHeal.mentalHeal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnboardingService {

    private final UserRepository userRepository;

    @Autowired
    public OnboardingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateOnboardingDetails(String email, OnboardingRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setProfession(request.getProfession());
        user.setPersonalitySummary(request.getPersonalitySummary());

        return userRepository.save(user);
    }
}
