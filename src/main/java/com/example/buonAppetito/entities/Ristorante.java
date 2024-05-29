package com.example.buonAppetito.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ristorante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String indirizzo;
    @ManyToOne
    @JoinColumn(name = "comune_id", nullable = false)
    @NotNull
    private Comune comune;
    @Column(nullable = false)
    private LocalTime orarioApertura;
    @Column(nullable = false)
    private LocalTime orarioChiusura;
    @OneToMany(mappedBy = "ristorante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menu;
    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;
    @OneToMany(mappedBy = "ristorante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tavolo> tavoli;
}