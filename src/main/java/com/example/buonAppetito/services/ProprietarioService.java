package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Ristorante;
import com.example.buonAppetito.entities.Utente;
import com.example.buonAppetito.enums.Role;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.exceptions.RoleMismatchException;
import com.example.buonAppetito.repositories.ComuneRepository;
import com.example.buonAppetito.repositories.RistoranteRepository;
import com.example.buonAppetito.repositories.UtenteRepository;
import com.example.buonAppetito.response.ProprietarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProprietarioService {

    @Autowired
    private ComuneRepository comuneRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private UtenteRepository utenteRepository;

    public ProprietarioResponse getProprietarioById(Long id) throws EntityNotFoundException, RoleMismatchException {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Utente"));
        if (utente.getRole() != Role.RISTORATORE) {
            throw new RoleMismatchException(id, "L'utente che stai cercando non è un Ristoratore");
        }
        return convertToResponse(utente);
    }

    public List<ProprietarioResponse> getAllProprietari() {
        List<Utente> ristoratori = utenteRepository.findAllByRole(Role.RISTORATORE);
        return ristoratori.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public void changeRoleToRistoratore(Long id) throws EntityNotFoundException {
        Utente proprietario = utenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Utente"));
        proprietario.setRole(Role.RISTORATORE);
        utenteRepository.saveAndFlush(proprietario);
    }

    public void associateRistorante(Long idProprietario, Long idRistorante) throws EntityNotFoundException {
        Utente proprietario = utenteRepository.findById(idProprietario)
                .orElseThrow(() -> new EntityNotFoundException(idProprietario, "Proprietario"));
        Ristorante ristorante = ristoranteRepository.findById(idRistorante)
                .orElseThrow(() -> new EntityNotFoundException(idRistorante, "Ristorante"));

        if (ristorante.getProprietario() != null) {
            throw new IllegalStateException("Questo ristorante ha già un proprietario assegnato.");
        }

        ristorante.setProprietario(proprietario);
        ristoranteRepository.saveAndFlush(ristorante);
    }

    public void dissociateRistorante(Long idRistorante) throws EntityNotFoundException {
        Ristorante ristorante = ristoranteRepository.findById(idRistorante)
                .orElseThrow(() -> new EntityNotFoundException(idRistorante, "Ristorante"));

        if (ristorante.getProprietario() == null) {
            throw new IllegalStateException("Non è stato assegnato nessun idProprietario, si prega di assegnare un nuovo idProprietario.");
        }

        ristorante.setProprietario(null);
        ristoranteRepository.saveAndFlush(ristorante);
    }

    private ProprietarioResponse convertToResponse(Utente utente) {
        List<Ristorante> ristoranti = ristoranteRepository.findAll().stream()
                .filter(r -> r.getProprietario() != null && r.getProprietario().getId().equals(utente.getId()))
                .collect(Collectors.toList());
        List<Long> idRistoranti = ristoranti.stream()
                .map(Ristorante::getId)
                .collect(Collectors.toList());
        List<String> nomiRistoranti = ristoranti.stream()
                .map(Ristorante::getNome)
                .collect(Collectors.toList());
        return ProprietarioResponse.builder()
                .id(utente.getId())
                .nome(utente.getNome())
                .cognome(utente.getCognome())
                .email(utente.getEmail())
                .nomeComune(utente.getComune() != null ? utente.getComune().getNome() : null)
                .indirizzo(utente.getIndirizzo())
                .telefono(utente.getTelefono())
                .dataNascita(utente.getDataNascita())
                .idRistoranti(idRistoranti != null ? idRistoranti : Collections.emptyList())
                .nomiRistoranti(nomiRistoranti != null ? nomiRistoranti : Collections.emptyList())
                .build();
    }
}
