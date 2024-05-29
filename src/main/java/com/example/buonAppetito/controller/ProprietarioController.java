package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.request.ProprietarioRequest;
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
    @Secured({"ADMIN"})
    public ResponseEntity<ProprietarioResponse> getProprietarioById(@PathVariable Long id) throws EntityNotFoundException {
        ProprietarioResponse proprietario = proprietarioService.getProprietarioById(id);
        return new ResponseEntity<>(proprietario, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Secured({"ADMIN"})
    public ResponseEntity<List<ProprietarioResponse>> getAllProprietari() {
        List<ProprietarioResponse> proprietari = proprietarioService.getAll();
        return new ResponseEntity<>(proprietari, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Secured({"ADMIN"})
    public ResponseEntity<ProprietarioResponse> createProprietario(@RequestBody ProprietarioRequest request) {
        try {
            ProprietarioResponse proprietario = proprietarioService.createProprietario(request);
            return new ResponseEntity<>(proprietario, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<ProprietarioResponse> updateProprietario(@PathVariable Long id, @RequestBody ProprietarioRequest updatedRequest) {
        try {
            ProprietarioResponse proprietario = proprietarioService.updateProprietario(id, updatedRequest);
            return new ResponseEntity<>(proprietario, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<?> deleteProprietario(@PathVariable Long id) {
        try {
            proprietarioService.deleteProprietarioById(id);
            return new ResponseEntity<>("Proprietario eliminato con successo", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
