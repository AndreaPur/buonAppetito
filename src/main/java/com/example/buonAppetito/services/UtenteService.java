package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Comune;
import com.example.buonAppetito.entities.Utente;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.ComuneRepository;
import com.example.buonAppetito.repositories.UtenteRepository;
import com.example.buonAppetito.request.UtenteRequest;
import com.example.buonAppetito.response.UtenteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ComuneRepository comuneRepository;

    public UtenteResponse getUtenteById(Long id) throws EntityNotFoundException {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Utente"));
        return convertToResponse(utente);
    }

    public List<UtenteResponse> getAll() {
        List<Utente> utenti = utenteRepository.findAll();
        return utenti.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public UtenteResponse createUtente(UtenteRequest request) throws EntityNotFoundException {
        Comune comune = Optional.ofNullable(comuneRepository.findComuneByNome(request.getNomeComune()))
                .orElseThrow(() -> new EntityNotFoundException(null, "Comune"));
        Utente utente = Utente.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .comune(comune)
                .indirizzo(request.getIndirizzo())
                .telefono(request.getTelefono())
                .dataNascita(request.getDataNascita())
                .password(request.getPassword())
                .build();
        utente = utenteRepository.saveAndFlush(utente);
        return convertToResponse(utente);
    }

    public UtenteResponse updateUtente(Long id, UtenteRequest updatedRequest) throws EntityNotFoundException {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Utente"));
        Comune comune = Optional.ofNullable(comuneRepository.findComuneByNome(updatedRequest.getNomeComune()))
                .orElseThrow(() -> new EntityNotFoundException(null, "Comune"));
        Utente updatedUtente = Utente.builder()
                .id(utente.getId())
                .nome(updatedRequest.getNome())
                .cognome(updatedRequest.getCognome())
                .email(updatedRequest.getEmail())
                .comune(comune)
                .indirizzo(updatedRequest.getIndirizzo())
                .telefono(updatedRequest.getTelefono())
                .dataNascita(updatedRequest.getDataNascita())
                .password(updatedRequest.getPassword())
                .registrationToken(utente.getRegistrationToken())
                .role(utente.getRole())
                .build();
        updatedUtente = utenteRepository.saveAndFlush(updatedUtente);
        return convertToResponse(updatedUtente);
    }

    public void deleteUtenteById(Long id) {
        utenteRepository.deleteById(id);
    }

    private UtenteResponse convertToResponse(Utente utente) {
        return UtenteResponse.builder()
                .id(utente.getId())
                .nome(utente.getNome())
                .cognome(utente.getCognome())
                .email(utente.getEmail())
                .nomeComune(utente.getComune().getNome())
                .indirizzo(utente.getIndirizzo())
                .telefono(utente.getTelefono())
                .dataNascita(utente.getDataNascita())
                .build();
    }
}
