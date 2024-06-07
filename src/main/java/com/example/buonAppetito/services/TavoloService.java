package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Ristorante;
import com.example.buonAppetito.entities.Tavolo;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.PrenotazioneRepository;
import com.example.buonAppetito.repositories.RistoranteRepository;
import com.example.buonAppetito.repositories.TavoloRepository;
import com.example.buonAppetito.request.TavoloRequest;
import com.example.buonAppetito.response.TavoloResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TavoloService {

    @Autowired
    private TavoloRepository tavoloRepository;
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;

    public TavoloResponse getTavoloById(Long id) {
        Optional<Tavolo> tavoloOptional = tavoloRepository.findById(id);
        return tavoloOptional.map(this::convertToResponse).orElse(null);
    }

    public List<TavoloResponse> getAll() {
        List<Tavolo> tavoli = tavoloRepository.findAll();
        return tavoli.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public TavoloResponse createTavolo(TavoloRequest request) throws EntityNotFoundException {
        Ristorante ristorante = ristoranteRepository.findById(request.getRistoranteId())
                .orElseThrow(() -> new EntityNotFoundException(request.getRistoranteId(), "Ristorante"));
        Tavolo tavolo = Tavolo.builder()
                .ristorante(ristorante)
                .posti(request.getPosti())
                .disponibile(true)
                .build();
        tavolo = tavoloRepository.saveAndFlush(tavolo);
        return convertToResponse(tavolo);
    }

    public TavoloResponse updateTavolo(Long id, TavoloRequest updatedRequest) throws EntityNotFoundException {
        Tavolo tavolo = tavoloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Tavolo"));
        Ristorante ristorante = ristoranteRepository.findById(updatedRequest.getRistoranteId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getRistoranteId(), "Ristorante"));
        tavolo.setRistorante(ristorante);
        tavolo.setPosti(updatedRequest.getPosti());
        tavolo = tavoloRepository.save(tavolo);
        return convertToResponse(tavolo);
    }

    public void deleteTavoloById(Long id) throws EntityNotFoundException {
        Tavolo tavolo = tavoloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Tavolo"));
        tavoloRepository.deleteById(id);
    }
    public boolean isAvailable(Long tavoloId, LocalDateTime orario, Long posti) {
        Optional<Tavolo> tavoloOptional = tavoloRepository.findById(tavoloId);
        if (tavoloOptional.isPresent()) {
            Tavolo tavolo = tavoloOptional.get();
            Ristorante ristorante = tavolo.getRistorante();
            boolean isAvailableAtTime = prenotazioneRepository
                    .findByTavoloAndOrarioBetween(tavolo, orario.minusHours(2), orario.plusHours(2))
                    .isEmpty();
            boolean isOrarioBeforeChiusura = orario.plusHours(2).isBefore(ristorante.getOrarioChiusura());
            boolean isOrarioAfterApertura = orario.isAfter(ristorante.getOrarioApertura());
            boolean isAvailable = tavolo.isDisponibile() && tavolo.getPosti() >= posti && isAvailableAtTime &&
                    isOrarioBeforeChiusura && isOrarioAfterApertura;
            return isAvailable;
        }
        return false;
    }

    public void liberaTavolo(Long tavoloId) throws EntityNotFoundException {
        Tavolo tavolo = tavoloRepository.findById(tavoloId)
                .orElseThrow(() -> new EntityNotFoundException(tavoloId, "Tavolo"));
        tavolo.setDisponibile(true);
        tavoloRepository.save(tavolo);
    }

    private TavoloResponse convertToResponse(Tavolo tavolo) {
        return TavoloResponse.builder()
                .id(tavolo.getId())
                .nomeRistorante(tavolo.getRistorante().getNome())
                .posti(tavolo.getPosti())
                .prenotazioneId(tavolo.getPrenotazione() != null ? tavolo.getPrenotazione().getId() : null)
                .disponibile(tavolo.isDisponibile())
                .build();
    }
}