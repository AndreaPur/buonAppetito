package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Conto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContoRepository extends JpaRepository<Conto, Long> {
}
