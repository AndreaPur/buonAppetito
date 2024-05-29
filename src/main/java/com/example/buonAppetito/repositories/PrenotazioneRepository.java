package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione,Long> {
}
