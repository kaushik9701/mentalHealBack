package com.mentalHeal.mentalHeal.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean onboardingComplete = false;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    // Optional demographic info
    private Integer age;
    private String gender;
    private String profession;
    // Add this for bidirectional mapping with JournalEntry:
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntry> journalEntries;

    // Similarly, you can add for FocusObjective if needed:
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FocusObjective> focusObjectives;

    // Optional emotional/personality data
     // Optional, but helps with clarity
    private String personalitySummary;// e.g., "Introverted, analytical, often anxious"


    private String traitScoresJson; // JSON string: {"introvert": 85, "overthinker": 70}

    private String topBadges; // Comma-separated: "introvert,empathetic,reflective"

    // Constructors
    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPersonalitySummary() {
        return personalitySummary;
    }

    public void setPersonalitySummary(String personalitySummary) {
        this.personalitySummary = personalitySummary;
    }

    public String getTraitScoresJson() {
        return traitScoresJson;
    }

    public void setTraitScoresJson(String traitScoresJson) {
        this.traitScoresJson = traitScoresJson;
    }

    public String getTopBadges() {
        return topBadges;
    }

    public void setTopBadges(String topBadges) {
        this.topBadges = topBadges;
    }
    public boolean isOnboardingComplete() {
        return onboardingComplete;
    }

    public void setOnboardingComplete(boolean onboardingComplete) {
        this.onboardingComplete = onboardingComplete;
    }
}
