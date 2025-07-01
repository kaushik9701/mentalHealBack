package com.mentalHeal.mentalHeal.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FocusCheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String note;


    @ManyToOne
    @JoinColumn(name = "focus_objective_id")
    @JsonBackReference
    private FocusObjective focusObjective;



}
