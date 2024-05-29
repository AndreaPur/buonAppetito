package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Comune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, Long> {

    @Query("SELECT c FROM Comune c WHERE c.nome = :nome")
    Comune findComuneByNome(@Param("nome") String nome);
}
