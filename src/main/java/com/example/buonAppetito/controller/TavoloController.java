package com.example.buonAppetito.controller;

import com.example.buonAppetito.request.TavoloRequest;
import com.example.buonAppetito.response.TavoloResponse;
import com.example.buonAppetito.services.TavoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tavolo")
public class TavoloController {

    @Autowired
    private TavoloService tavoloService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> getTavoloById(@PathVariable Long id) {
        try {
            TavoloResponse tavolo = tavoloService.getTavoloById(id);
            return new ResponseEntity<>(tavolo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero del tavolo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Secured({"ADMIN"})
    public ResponseEntity<?> getAllTavoli() {
        try {
            List<TavoloResponse> tavoli = tavoloService.getAll();
            if (!tavoli.isEmpty()) {
                return new ResponseEntity<>(tavoli, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessun tavolo trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero dei tavoli", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> createTavolo(@RequestBody TavoloRequest request) {
        try {
            TavoloResponse createdTavolo = tavoloService.createTavolo(request);
            return new ResponseEntity<>(createdTavolo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la creazione del tavolo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> updateTavolo(@PathVariable Long id, @RequestBody TavoloRequest updatedRequest) {
        try {
            TavoloResponse updatedTavolo = tavoloService.updateTavolo(id, updatedRequest);
            if (updatedTavolo != null) {return new ResponseEntity<>(updatedTavolo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Tavolo non trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiornamento del tavolo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deleteTavolo(@PathVariable Long id) {
        try {
            tavoloService.deleteTavoloById(id);
            return new ResponseEntity<>("Tavolo eliminato con successo", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'eliminazione del tavolo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
