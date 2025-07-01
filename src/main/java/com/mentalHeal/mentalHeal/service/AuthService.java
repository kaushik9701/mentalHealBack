package com.mentalHeal.mentalHeal.service;

import com.mentalHeal.mentalHeal.DTO.LoginRequest;
import com.mentalHeal.mentalHeal.model.User;

public interface AuthService {
    User registerUser(User user);
    String login(LoginRequest request);
}
