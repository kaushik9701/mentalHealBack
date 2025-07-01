package com.mentalHeal.mentalHeal.repository;

import com.mentalHeal.mentalHeal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Needed for login
}

