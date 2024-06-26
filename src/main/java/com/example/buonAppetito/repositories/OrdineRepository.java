package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
}
