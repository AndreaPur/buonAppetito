package com.example.buonAppetito.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private double totale;
    @ManyToOne
    @JoinColumn(name = "tavolo_id", nullable = false)
    @JsonBackReference
    private Tavolo tavolo;
    @OneToMany(mappedBy = "conto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ordine> ordini;
    @Column(nullable = false)
    private LocalDateTime timestamp;
}