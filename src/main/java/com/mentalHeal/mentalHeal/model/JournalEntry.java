package com.mentalHeal.mentalHeal.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String insight; // ✅ Added this line

    private OffsetDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors
    public JournalEntry() {
        this.timestamp = OffsetDateTime.now();
    }

    public JournalEntry(String title, String content, String insight, User user) { // ✅ Updated constructor
        this.title = title;
        this.content = content;
        this.insight = insight;
        this.user = user;
        this.timestamp = OffsetDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInsight() { // ✅ Getter
        return insight;
    }

    public void setInsight(String insight) { // ✅ Setter
        this.insight = insight;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
