package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.request.ComuneRequest;
import com.example.buonAppetito.response.ComuneResponse;
import com.example.buonAppetito.services.ComuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comune")
public class ComuneController {

    @Autowired
    private ComuneService comuneService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> getComuneById(@PathVariable Long id) {
        try {
            ComuneResponse comune = comuneService.getComuneById(id);
            return new ResponseEntity<>(comune, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero del comune", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> getAllComuni() {
        try {
            List<ComuneResponse> comuni = comuneService.getAll();
            if (!comuni.isEmpty()) {
                return new ResponseEntity<>(comuni, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessun comune trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero dei comuni", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/create")
    @Secured({"ADMIN"})
    public ResponseEntity<?> createComune(@RequestBody ComuneRequest request) {
        try {
            ComuneResponse createdComune = comuneService.createComune(request);
            return new ResponseEntity<>(createdComune, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la creazione del comune", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> updateComune(@PathVariable Long id, @RequestBody ComuneRequest updatedComuneRequest) {
        try {
            ComuneResponse updatedComune = comuneService.updateComune(id, updatedComuneRequest);
            return new ResponseEntity<>(updatedComune, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiornamento del comune", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deleteComune(@PathVariable Long id) {
        try {
            comuneService.deleteComuneById(id);
            return new ResponseEntity<>("Comune eliminato con successo", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'eliminazione del comune", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
