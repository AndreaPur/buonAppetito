package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.request.ContoRequest;
import com.example.buonAppetito.response.ContoResponse;
import com.example.buonAppetito.services.ContoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conto")
public class ContoController {

    @Autowired
    private ContoService contoService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> getContoById(@PathVariable Long id) {
        try {
            ContoResponse conto = contoService.getContoById(id);
            return new ResponseEntity<>(conto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero del conto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Secured({"ADMIN"})
    public ResponseEntity<?> getAllConti() {
        try {
            List<ContoResponse> conti = contoService.getAll();
            if (!conti.isEmpty()) {
                return new ResponseEntity<>(conti, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessun conto trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero dei conti", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @Secured({"ADMIN"})
    public ResponseEntity<?> createConto(@RequestBody ContoRequest request) {
        try {
            ContoResponse createdConto = contoService.createConto(request);
            return new ResponseEntity<>(createdConto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la creazione del conto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> updateConto(@PathVariable Long id, @RequestBody ContoRequest updatedContoRequest) {
        try {
            ContoResponse updatedConto = contoService.updateConto(id, updatedContoRequest);
            return new ResponseEntity<>(updatedConto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiornamento del conto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deleteConto(@PathVariable Long id) {
        try {
            contoService.deleteContoById(id);
            return new ResponseEntity<>("Conto eliminato con successo", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'eliminazione del conto", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}