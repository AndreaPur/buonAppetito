package com.example.buonAppetito.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordine")
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
    @JoinColumn(name = "prenotazione_id", nullable = false)
    private Prenotazione prenotazione;
    @ManyToMany
    @JoinTable(
            name = "ordine_piatti",
            joinColumns = @JoinColumn(name = "ordine_id"),
            inverseJoinColumns = @JoinColumn(name = "piatto_id")
    )
    private List<Piatto> piatti;
    @Column(nullable = false)
    private double totale;
    @Column(nullable = false)
    private LocalDateTime creazioneOrdine;
    @Column
    private LocalDateTime chiusuraOrdine;
    @Column(nullable = false)
    private boolean chiuso = false;

}
