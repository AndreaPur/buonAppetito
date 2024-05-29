package com.example.buonAppetito.controller;

import com.example.buonAppetito.request.PrenotazioneRequest;
import com.example.buonAppetito.response.PrenotazioneResponse;
import com.example.buonAppetito.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<PrenotazioneResponse> getPrenotazioneById(@PathVariable Long id) {
        PrenotazioneResponse prenotazione = prenotazioneService.getPrenotazioneById(id);
        return new ResponseEntity<>(prenotazione, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<List<PrenotazioneResponse>> getAllPrenotazioni() {
        List<PrenotazioneResponse> prenotazioni = prenotazioneService.getAll();
        return new ResponseEntity<>(prenotazioni, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<PrenotazioneResponse> createPrenotazione(@RequestBody PrenotazioneRequest request) {
        try {
            PrenotazioneResponse prenotazione = prenotazioneService.createPrenotazione(request);
            return new ResponseEntity<>(prenotazione, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<PrenotazioneResponse> updatePrenotazione(@PathVariable Long id, @RequestBody PrenotazioneRequest updatedRequest) {
        try {
            PrenotazioneResponse prenotazione = prenotazioneService.updatePrenotazione(id, updatedRequest);
            return new ResponseEntity<>(prenotazione, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> deletePrenotazione(@PathVariable Long id) {
        try {
            prenotazioneService.deletePrenotazioneById(id);
            return new ResponseEntity<>("Prenotazione eliminata con successo", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
