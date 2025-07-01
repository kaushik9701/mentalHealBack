package com.mentalHeal.mentalHeal.controller;

import com.mentalHeal.mentalHeal.model.FocusObjective;
import com.mentalHeal.mentalHeal.model.User;
import com.mentalHeal.mentalHeal.repository.UserRepository;
import com.mentalHeal.mentalHeal.service.FocusObjectiveService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/focus")
public class FocusObjectiveController {

    private final UserRepository userRepository;
    private final FocusObjectiveService focusObjectiveService;

    public FocusObjectiveController(UserRepository userRepository,
                                    FocusObjectiveService focusObjectiveService) {
        this.userRepository = userRepository;
        this.focusObjectiveService = focusObjectiveService;
    }

    // 🎯 GET /focus/getObjectives -> returns all focus objectives for the authenticated user
    @GetMapping("/getObjectives")
    public ResponseEntity<List<FocusObjective>> getObjectives(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        try {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<FocusObjective> objectives = focusObjectiveService.getObjectivesByUser(user);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectives);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.emptyList());
        }
    }
}
