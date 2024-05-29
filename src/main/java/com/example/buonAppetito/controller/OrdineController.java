package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.request.OrdineRequest;
import com.example.buonAppetito.response.OrdineResponse;
import com.example.buonAppetito.services.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordine")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> getOrdineById(@PathVariable Long id) {
        try {
            OrdineResponse ordine = ordineService.getOrdineById(id);
            return new ResponseEntity<>(ordine, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero dell'ordine", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Secured({"ADMIN"})
    public ResponseEntity<?> getAllOrdini() {
        try {
            List<OrdineResponse> ordini = ordineService.getAll();
            if (!ordini.isEmpty()) {
                return new ResponseEntity<>(ordini, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessun ordine trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero degli ordini", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> createOrdine(@RequestBody OrdineRequest request) {
        try {
            OrdineResponse createdOrdine = ordineService.createOrdine(request);
            return new ResponseEntity<>(createdOrdine, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la creazione dell'ordine", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> updateOrdine(@PathVariable Long id, @RequestBody OrdineRequest updatedOrdineRequest) {
        try {
            OrdineResponse updatedOrdine = ordineService.updateOrdine(id, updatedOrdineRequest);
            return new ResponseEntity<>(updatedOrdine, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiornamento dell'ordine", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> deleteOrdine(@PathVariable Long id) {
        try {
            ordineService.deleteOrdineById(id);
            return new ResponseEntity<>("Ordine eliminato con successo", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'eliminazione dell'ordine", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
