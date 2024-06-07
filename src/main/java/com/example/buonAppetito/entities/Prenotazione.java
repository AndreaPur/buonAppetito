package com.example.buonAppetito.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
    @ManyToOne
    @JoinColumn(name = "ristorante_id", nullable = false)
    @NotNull
    private Ristorante ristorante;
    @OneToOne
    @JoinColumn(name = "tavolo_id", nullable = false)
    @NotNull
    private Tavolo tavolo;
    @Column(nullable = false)
    private Long posti;
    @Column(nullable = false)
    private LocalDateTime orario;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @OneToMany(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ordine> ordini;
}