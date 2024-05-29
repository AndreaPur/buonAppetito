package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Ristorante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RistoranteRepository extends JpaRepository<Ristorante,Long> {
}
