package com.example.buonAppetito.controller;

import com.example.buonAppetito.request.RistoranteRequest;
import com.example.buonAppetito.response.RistoranteResponse;
import com.example.buonAppetito.services.RistoranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ristorante")
public class RistoranteController {

    @Autowired
    private RistoranteService ristoranteService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> getRistoranteById(@PathVariable Long id) {
        try {
            RistoranteResponse ristorante = ristoranteService.getRistoranteById(id);
            return new ResponseEntity<>(ristorante, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero del ristorante", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> getAllRistoranti() {
        try {
            List<RistoranteResponse> ristoranti = ristoranteService.getAll();
            if (!ristoranti.isEmpty()) {
                return new ResponseEntity<>(ristoranti, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessun ristorante trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero dei ristoranti", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> createRistorante(@RequestBody RistoranteRequest request) {
        try {
            RistoranteResponse createdRistorante = ristoranteService.createRistorannte(request);
            return new ResponseEntity<>(createdRistorante, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la creazione del ristorante", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> updateRistorante(@PathVariable Long id, @RequestBody RistoranteRequest updatedRistoranteRequest) {
        try {
            RistoranteResponse updatedRistorante = ristoranteService.updateRistorante(id, updatedRistoranteRequest);
            if (updatedRistorante != null) {
                return new ResponseEntity<>(updatedRistorante, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Ristorante non trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiornamento del ristorante", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deleteRistorante(@PathVariable Long id) {
        try {
            ristoranteService.deleteRistoranteById(id);
            return new ResponseEntity<>("Ristorante eliminato con successo", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'eliminazione del ristorante", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
