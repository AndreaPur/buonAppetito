package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.request.UtenteRequest;
import com.example.buonAppetito.response.UtenteResponse;
import com.example.buonAppetito.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<UtenteResponse> getUtenteById(@PathVariable Long id) throws EntityNotFoundException {
        UtenteResponse utente = utenteService.getUtenteById(id);
        return new ResponseEntity<>(utente, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Secured({"ADMIN"})
    public ResponseEntity<List<UtenteResponse>> getAllUtenti() {
        List<UtenteResponse> utenti = utenteService.getAll();
        return new ResponseEntity<>(utenti, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Secured({"ADMIN"})
    public ResponseEntity<UtenteResponse> createUtente(@RequestBody UtenteRequest request) {
        try {
            UtenteResponse utente = utenteService.createUtente(request);
            return new ResponseEntity<>(utente, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<UtenteResponse> updateUtente(@PathVariable Long id, @RequestBody UtenteRequest updatedRequest) {
        try {
            UtenteResponse utente = utenteService.updateUtente(id, updatedRequest);
            return new ResponseEntity<>(utente, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deleteUtente(@PathVariable Long id) {
        try {
            utenteService.deleteUtenteById(id);
            return new ResponseEntity<>("Utente eliminato con successo", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
