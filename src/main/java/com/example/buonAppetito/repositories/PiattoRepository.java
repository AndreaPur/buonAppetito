package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Piatto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PiattoRepository extends JpaRepository<Piatto,Long> {
}
