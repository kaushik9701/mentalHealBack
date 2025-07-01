package com.mentalHeal.mentalHeal.repository;

import com.mentalHeal.mentalHeal.model.JournalEntry;
import com.mentalHeal.mentalHeal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUser(User user);
}