package com.mentalHeal.mentalHeal.controller;


import com.mentalHeal.mentalHeal.DTO.JournalEntryDto;
import com.mentalHeal.mentalHeal.model.JournalEntry;
import com.mentalHeal.mentalHeal.model.User;
import com.mentalHeal.mentalHeal.repository.JournalEntryRepository;
import com.mentalHeal.mentalHeal.repository.UserRepository;

import com.mentalHeal.mentalHeal.service.JournalEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryRepository repository;
    private final UserRepository userRepository;
    private final JournalEntryService journalEntryService;

    public JournalEntryController(JournalEntryRepository repository,
                                  UserRepository userRepository,
                                  JournalEntryService journalEntryService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.journalEntryService = journalEntryService; // assign here!
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEntries(Principal principal) {
        try {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<JournalEntry> entries = repository.findByUser(user);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(entries);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.emptyList());
        }
    }

    @PostMapping
    public ResponseEntity<?> addEntry(@RequestBody JournalEntryDto entryDto,
                                      Principal principal) {
        System.out.println("Principal (POST): " + (principal != null ? principal.getName() : "null"));

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

        try {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Use the service to create the entry with AI-generated title + insight
            JournalEntry savedEntry = journalEntryService.createEntryWithAI(entryDto.getContent(), user);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(savedEntry);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}