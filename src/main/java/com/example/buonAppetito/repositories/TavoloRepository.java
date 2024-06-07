package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Tavolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TavoloRepository extends JpaRepository<Tavolo, Long> {

    @Query("SELECT t FROM Tavolo t WHERE t.ristorante.id = :ristoranteId AND t.disponibile = true")
    List<Tavolo> findAvailableTavoliByRistoranteId(@Param("ristoranteId") Long ristoranteId);
}
