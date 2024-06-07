package com.example.buonAppetito.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "piatto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Piatto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private Double prezzo;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

}