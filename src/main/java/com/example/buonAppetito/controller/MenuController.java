package com.example.buonAppetito.controller;

import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.request.MenuRequest;
import com.example.buonAppetito.response.MenuResponse;
import com.example.buonAppetito.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/get/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public ResponseEntity<?> getMenuById(@PathVariable Long id) {
        try {
            MenuResponse menu = menuService.getMenuById(id);
            return new ResponseEntity<>(menu, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero del menu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> getAllMenu() {
        try {
            List<MenuResponse> menu = menuService.getAllMenu();
            if (!menu.isEmpty()) {
                return new ResponseEntity<>(menu, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessun menu trovato", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero dei menu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> createMenu(@RequestBody MenuRequest request) {
        try {
            MenuResponse createdMenu = menuService.createMenu(request);
            return new ResponseEntity<>(createdMenu, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la creazione del menu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> updateMenu(@PathVariable Long id, @RequestBody MenuRequest updatedMenuRequest) throws EntityNotFoundException {
        try {
            MenuResponse updatedMenu = menuService.updateMenu(id, updatedMenuRequest);
            return new ResponseEntity<>(updatedMenu, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiornamento del menu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ADMIN", "RISTORATORE"})
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        try {
            menuService.deleteMenuById(id);
            return new ResponseEntity<>("Menu eliminato con successo", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'eliminazione del menu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}