package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.exceptions.ErrorResponse;
import com.example.buonAppetito.exceptions.NoAvailabilityException;
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
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<PrenotazioneResponse> getPrenotazioneById(@PathVariable Long id) {
        PrenotazioneResponse prenotazione = prenotazioneService.getPrenotazioneById(id);
        return new ResponseEntity<>(prenotazione, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<List<PrenotazioneResponse>> getAllPrenotazioni() {
        List<PrenotazioneResponse> prenotazioni = prenotazioneService.getAll();
        return new ResponseEntity<>(prenotazioni, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> createPrenotazione(@RequestBody PrenotazioneRequest request) throws EntityNotFoundException, NoAvailabilityException {
        try {
            PrenotazioneResponse prenotazione = prenotazioneService.createPrenotazione(request);
            return new ResponseEntity<>(prenotazione, HttpStatus.CREATED);
        } catch (NoAvailabilityException e) {
            return new ResponseEntity<>(new ErrorResponse("No availability", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Entity not found", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal Server Error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> updatePrenotazione(@PathVariable Long id, @RequestBody PrenotazioneRequest updatedRequest) {
        try {
            PrenotazioneResponse prenotazione = prenotazioneService.updatePrenotazione(id, updatedRequest);
            return new ResponseEntity<>(prenotazione, HttpStatus.OK);
        } catch (NoAvailabilityException e) {
            return new ResponseEntity<>(new ErrorResponse("No availability", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Entity not found", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal Server Error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> deletePrenotazione(@PathVariable Long id) {
        try {
            prenotazioneService.deletePrenotazioneById(id);
            return new ResponseEntity<>("Prenotazione eliminata con successo", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Entity not found", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Internal Server Error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}