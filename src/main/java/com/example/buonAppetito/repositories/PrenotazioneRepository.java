package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Prenotazione;
import com.example.buonAppetito.entities.Tavolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    @Query("SELECT p FROM Prenotazione p WHERE p.tavolo = :tavolo AND p.orario BETWEEN :start AND :end")
    List<Prenotazione> findByTavoloAndOrarioBetween(@Param("tavolo") Tavolo tavolo, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT SUM(p.posti) FROM Prenotazione p WHERE p.tavolo.id = :tavoloId AND p.orario = :orario")
    Long countReservedSeats(@Param("tavoloId") Long tavoloId, @Param("orario") LocalDateTime orario);
}
