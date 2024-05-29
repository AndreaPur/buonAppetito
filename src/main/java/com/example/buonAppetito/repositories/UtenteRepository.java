package com.example.buonAppetito.repositories;

import com.example.buonAppetito.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente,Long> {

    @Query(value = "SELECT * FROM utente u WHERE u.email = :email", nativeQuery = true)
    Utente findUtenteByEmail(@Param("email") String email);

}
