package com.mentalHeal.mentalHeal.service;

import com.mentalHeal.mentalHeal.DTO.LoginRequest;
import com.mentalHeal.mentalHeal.exceptions.InvalidCredentialsException;
import com.mentalHeal.mentalHeal.exceptions.UserAlreadyExistsException;
import com.mentalHeal.mentalHeal.model.User;
import com.mentalHeal.mentalHeal.repository.UserRepository;
import com.mentalHeal.mentalHeal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;


    // Register user method
    public User registerUser(User user) {
        // Basic validation: Check if the user already exists

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Save the user to the database
        return userRepository.save(user);
    }

    public String login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        System.out.println("User found: " + user.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        System.out.println("Generated token: " + token);
        // <-- you’ll create this utility next

        return token;
    }
}
