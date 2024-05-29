package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Tavolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TavoloRepository extends JpaRepository<Tavolo,Long> {
}
