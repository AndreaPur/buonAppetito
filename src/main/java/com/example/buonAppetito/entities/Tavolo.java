package com.example.buonAppetito.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tavolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ristorante_id", nullable = false)
    @JsonBackReference
    private Ristorante ristorante;
    @OneToOne(mappedBy = "tavolo")
    private Prenotazione prenotazione;
    @Column(nullable = false)
    private Long posti;
    @Column(nullable = false)
    private boolean disponibile = true;

}