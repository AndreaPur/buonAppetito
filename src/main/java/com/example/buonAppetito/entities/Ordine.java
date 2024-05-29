package com.example.buonAppetito.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "conto_id", nullable = false)
    private Conto conto;
    @ManyToOne
    @JoinColumn(name = "piatto_id", nullable = false)
    private Piatto piatto;
    @Column(nullable = false)
    private int quantita;
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
