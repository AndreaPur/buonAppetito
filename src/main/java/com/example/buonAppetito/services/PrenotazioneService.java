package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Prenotazione;
import com.example.buonAppetito.entities.Ristorante;
import com.example.buonAppetito.entities.Tavolo;
import com.example.buonAppetito.entities.Utente;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.exceptions.NoAvailabilityException;
import com.example.buonAppetito.repositories.PrenotazioneRepository;
import com.example.buonAppetito.repositories.RistoranteRepository;
import com.example.buonAppetito.repositories.TavoloRepository;
import com.example.buonAppetito.repositories.UtenteRepository;
import com.example.buonAppetito.request.PrenotazioneRequest;
import com.example.buonAppetito.response.PrenotazioneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private TavoloRepository tavoloRepository;
    @Autowired
    private TavoloService tavoloService;

    public PrenotazioneResponse getPrenotazioneById(Long id) {
        Optional<Prenotazione> prenotazioneOptional = prenotazioneRepository.findById(id);
        return prenotazioneOptional.map(this::convertToResponse).orElse(null);
    }

    public List<PrenotazioneResponse> getAll() {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        return prenotazioni.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public PrenotazioneResponse createPrenotazione(PrenotazioneRequest request) throws EntityNotFoundException, NoAvailabilityException {
        Utente utente = utenteRepository.findById(request.getUtenteId())
                .orElseThrow(() -> new EntityNotFoundException(request.getUtenteId(), "Utente"));
        Ristorante ristorante = ristoranteRepository.findById(request.getRistoranteId())
                .orElseThrow(() -> new EntityNotFoundException(request.getRistoranteId(), "Ristorante"));
        Tavolo tavolo = tavoloRepository.findById(request.getTavoloId())
                .orElseThrow(() -> new EntityNotFoundException(request.getTavoloId(), "Tavolo"));
        if (!tavoloService.isAvailable(tavolo.getId(), request.getOrario(), request.getPosti())) {
            throw new NoAvailabilityException("Non ci sono posti disponibili per questa prenotazione.");
        }
        Prenotazione prenotazione = Prenotazione.builder()
                .utente(utente)
                .ristorante(ristorante)
                .tavolo(tavolo)
                .orario(request.getOrario())
                .posti(request.getPosti())
                .timestamp(LocalDateTime.now())
                .build();
        prenotazione = prenotazioneRepository.saveAndFlush(prenotazione);
        ristorante.setPostiDisponibili(ristorante.getPostiDisponibili() - request.getPosti());
        if (ristorante.getPostiDisponibili() < 0) {
            ristorante.setPostiDisponibili(0L);
        }
        ristoranteRepository.save(ristorante);
        return convertToResponse(prenotazione);
    }

    public PrenotazioneResponse updatePrenotazione(Long id, PrenotazioneRequest updatedRequest) throws EntityNotFoundException, NoAvailabilityException {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Prenotazione"));
        Utente utente = utenteRepository.findById(updatedRequest.getUtenteId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getUtenteId(), "Utente"));
        Ristorante ristorante = ristoranteRepository.findById(updatedRequest.getRistoranteId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getRistoranteId(), "Ristorante"));
        Tavolo tavolo = tavoloRepository.findById(updatedRequest.getTavoloId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getTavoloId(), "Tavolo"));
        if (!tavoloService.isAvailable(tavolo.getId(), updatedRequest.getOrario(), updatedRequest.getPosti())) {
            throw new NoAvailabilityException("Non ci sono posti disponibili per questa prenotazione.");
        }
        ristorante.setPostiDisponibili(ristorante.getPostiDisponibili() + prenotazione.getPosti());
        prenotazione.setUtente(utente);
        prenotazione.setRistorante(ristorante);
        prenotazione.setTavolo(tavolo);
        prenotazione.setOrario(updatedRequest.getOrario());
        prenotazione.setPosti(updatedRequest.getPosti());
        prenotazione = prenotazioneRepository.save(prenotazione);
        ristorante.setPostiDisponibili(ristorante.getPostiDisponibili() - updatedRequest.getPosti());
        tavolo.setDisponibile(false);
        ristoranteRepository.save(ristorante);
        tavoloRepository.save(tavolo);
        return convertToResponse(prenotazione);
    }

    public void deletePrenotazioneById(Long id) throws EntityNotFoundException {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Prenotazione"));
        Ristorante ristorante = prenotazione.getRistorante();
        ristorante.setPostiDisponibili(Math.min(ristorante.getPostiDisponibili() + prenotazione.getPosti(), ristorante.getPostiTotali()));
        Tavolo tavolo = prenotazione.getTavolo();
        tavolo.setDisponibile(true);
        ristoranteRepository.save(ristorante);
        tavoloRepository.save(tavolo);
        prenotazioneRepository.deleteById(id);
    }

    public void liberaPostiPrenotazione(Long prenotazioneId) throws EntityNotFoundException {
        Optional<Prenotazione> prenotazioneOptional = prenotazioneRepository.findById(prenotazioneId);
        if (prenotazioneOptional.isPresent()) {
            Prenotazione prenotazione = prenotazioneOptional.get();
            Tavolo tavolo = prenotazione.getTavolo();
            Ristorante ristorante = tavolo.getRistorante();
            ristorante.setPostiDisponibili(Math.min(ristorante.getPostiDisponibili() + prenotazione.getPosti(), ristorante.getPostiTotali()));
            tavolo.setDisponibile(true);
            ristoranteRepository.save(ristorante);
            tavoloRepository.save(tavolo);
        } else {
            throw new EntityNotFoundException(prenotazioneId, "Prenotazione");
        }
    }

    private PrenotazioneResponse convertToResponse(Prenotazione prenotazione) {
        return PrenotazioneResponse.builder()
                .id(prenotazione.getId())
                .nomeUtente(prenotazione.getUtente().getNome())
                .cognomeUtente(prenotazione.getUtente().getCognome())
                .nomeRistorante(prenotazione.getRistorante().getNome())
                .nomeComune(prenotazione.getRistorante().getComune().getNome())
                .tavoloId(prenotazione.getTavolo().getId())
                .posti(prenotazione.getPosti())
                .orario(prenotazione.getOrario())
                .timestamp(prenotazione.getTimestamp())
                .build();
    }
}