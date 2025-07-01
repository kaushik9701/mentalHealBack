package com.mentalHeal.mentalHeal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FocusSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    // In Suggestion.java
    @ManyToOne
    @JoinColumn(name = "focus_objective_id")
    @JsonBackReference
    private FocusObjective focusObjective;

}
