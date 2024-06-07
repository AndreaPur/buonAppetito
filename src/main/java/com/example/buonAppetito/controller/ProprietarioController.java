package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.exceptions.RoleMismatchException;
import com.example.buonAppetito.response.ProprietarioResponse;
import com.example.buonAppetito.services.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proprietari")
public class ProprietarioController {

    @Autowired
    private ProprietarioService proprietarioService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> getProprietarioById(@PathVariable Long id) {
        try {
            ProprietarioResponse proprietario = proprietarioService.getProprietarioById(id);
            return new ResponseEntity<>(proprietario, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RoleMismatchException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    @Secured({"ADMIN"})
    public ResponseEntity<List<ProprietarioResponse>> getAllProprietari() {
        List<ProprietarioResponse> proprietari = proprietarioService.getAllProprietari();
        return new ResponseEntity<>(proprietari, HttpStatus.OK);
    }

    @PutMapping("/changeRoleToRistoratore/{id}")
    @Secured({"UTENTE","ADMIN"})
    public ResponseEntity<?> changeRoleToRistoratore(@PathVariable Long id) throws EntityNotFoundException {
        try {
            proprietarioService.changeRoleToRistoratore(id);
            return new ResponseEntity<>("Ruolo aggiornato con successo", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("EntityNotFoundException",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/associateRistorante/{idProprietario}/{idRistorante}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> associateRistorante(@PathVariable Long idProprietario, @PathVariable Long idRistorante) {
        try {
            proprietarioService.associateRistorante(idProprietario, idRistorante);
            return new ResponseEntity<>(String.format("Il ristorante con id %d è stato associato all'utente con id %d" ,idRistorante , idProprietario),HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("EntityNotFoundException",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/dissociaRistorante/{idRistorante}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> dissociateRistorante(@PathVariable Long idRistorante) {
        try {
            proprietarioService.dissociateRistorante(idRistorante);
            return new ResponseEntity<>("Il ristorante è senza proprietario, si prega di assegnare un nuovo proprietario",HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("EntityNotFoundException",HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}