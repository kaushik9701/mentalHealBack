package com.mentalHeal.mentalHeal.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

// ... imports remain unchanged

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FocusObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String objective;

    private LocalDate createdAt;
    private LocalDate lastCheckIn;
    private LocalDate nextNudgeDate;
    private boolean completed;

    @OneToMany(mappedBy = "focusObjective", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FocusCheckIn> checkIns = new java.util.ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
