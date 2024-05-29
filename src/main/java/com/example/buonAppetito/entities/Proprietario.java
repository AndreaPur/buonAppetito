package com.example.buonAppetito.entities;

import com.example.buonAppetito.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Proprietario extends Utente {

    @OneToMany(mappedBy = "proprietario")
    private List<Ristorante> ristoranti;

    public Proprietario(Long id, String nome, String cognome, String email, Comune comune, String indirizzo,
                        String telefono, LocalDate dataNascita, String password, String registrationToken,
                        Role role, List<Prenotazione> prenotazioni, List<Ristorante> ristoranti) {
        super(id, nome, cognome, email, comune, indirizzo, telefono, dataNascita, password, registrationToken, role, prenotazioni);
        this.ristoranti = ristoranti;
    }
}