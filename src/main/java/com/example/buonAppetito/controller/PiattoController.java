package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.request.PiattoRequest;
import com.example.buonAppetito.response.PiattoResponse;
import com.example.buonAppetito.services.PiattoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/piatto")
public class PiattoController {

    @Autowired
    private PiattoService piattoService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> getPiattoById(@PathVariable Long id) {
        try {
            PiattoResponse piatto = piattoService.getPiattoById(id);
            return new ResponseEntity<>(piatto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero del piatto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Secured({"ADMIN"})
    public ResponseEntity<?> getAllPiatti() {
        try {
            List<PiattoResponse> piatti = piattoService.getAll();
            if (!piatti.isEmpty()) {
                return new ResponseEntity<>(piatti, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessun piatto trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero dei piatti", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> createPiatto(@RequestBody PiattoRequest request) {
        try {
            PiattoResponse createdPiatto = piattoService.createPiatto(request);
            return new ResponseEntity<>(createdPiatto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la creazione del piatto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> updatePiatto(@PathVariable Long id, @RequestBody PiattoRequest updatedPiattoRequest) {
        try {
            PiattoResponse updatedPiatto = piattoService.updatePiatto(id, updatedPiattoRequest);
            return new ResponseEntity<>(updatedPiatto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiornamento del piatto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deletePiatto(@PathVariable Long id) {
        try {
            piattoService.deletePiattoById(id);
            return new ResponseEntity<>("Piatto eliminato con successo", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'eliminazione del piatto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}