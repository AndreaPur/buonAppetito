package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.*;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.exceptions.InvalidOrderException;
import com.example.buonAppetito.repositories.OrdineRepository;
import com.example.buonAppetito.repositories.PiattoRepository;
import com.example.buonAppetito.repositories.PrenotazioneRepository;
import com.example.buonAppetito.repositories.RistoranteRepository;
import com.example.buonAppetito.request.OrdineRequest;
import com.example.buonAppetito.response.OrdineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdineService {

    @Autowired
    private OrdineRepository ordineRepository;
    @Autowired
    private PiattoRepository piattoRepository;
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private PrenotazioneService prenotazioneService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private TavoloService tavoloService;
    @Autowired
    private FileService fileService;

    public OrdineResponse getOrdineById(Long id) throws EntityNotFoundException {
        Ordine ordine = ordineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ordine"));
        return convertToResponse(ordine);
    }

    public List<OrdineResponse> getAll() {
        List<Ordine> ordini = ordineRepository.findAll();
        return ordini.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public OrdineResponse createOrdine(OrdineRequest request) throws EntityNotFoundException, InvalidOrderException {
        Prenotazione prenotazione = prenotazioneRepository.findById(request.getPrenotazioneId())
                .orElseThrow(() -> new EntityNotFoundException(request.getPrenotazioneId(), "Prenotazione"));
        if (LocalDateTime.now().isBefore(prenotazione.getOrario())) {
            throw new InvalidOrderException("Non è possibile creare un ordine prima dell'orario della prenotazione.");
        }
        List<Piatto> piatti = request.getPiattiId().stream()
                .map(piattoId -> {
                    try {
                        return piattoRepository.findById(piattoId)
                                .orElseThrow(() -> new EntityNotFoundException(piattoId, "Piatto"));
                    } catch (EntityNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        double totale = piatti.stream().mapToDouble(Piatto::getPrezzo).sum();
        Ordine ordine = Ordine.builder()
                .prenotazione(prenotazione)
                .piatti(piatti)
                .totale(totale)
                .creazioneOrdine(LocalDateTime.now())
                .build();
        Ordine savedOrdine = ordineRepository.saveAndFlush(ordine);
        return convertToResponse(savedOrdine);
    }

    public OrdineResponse updateOrdine(Long id, OrdineRequest updatedRequest) throws EntityNotFoundException {
        Ordine existingOrdine = ordineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ordine"));
        Prenotazione prenotazione = prenotazioneRepository.findById(updatedRequest.getPrenotazioneId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getPrenotazioneId(), "Prenotazione"));
        List<Piatto> piatti = updatedRequest.getPiattiId().stream()
                .map(piattoId -> {
                    try {
                        return piattoRepository.findById(piattoId)
                                .orElseThrow(() -> new EntityNotFoundException(piattoId, "Piatto"));
                    } catch (EntityNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        double totale = piatti.stream().mapToDouble(Piatto::getPrezzo).sum();
        existingOrdine.setPrenotazione(prenotazione);
        existingOrdine.setPiatti(piatti);
        existingOrdine.setTotale(totale);
        Ordine updatedOrdine = ordineRepository.saveAndFlush(existingOrdine);
        return convertToResponse(updatedOrdine);
    }

    public void deleteOrdineById(Long id) throws EntityNotFoundException {
        if (!ordineRepository.existsById(id)) {
            throw new EntityNotFoundException(id, "Ordine");
        }
        ordineRepository.deleteById(id);
    }

    public OrdineResponse aggiungiPiattiAllOrdine(Long ordineId, List<Long> piattiId) throws EntityNotFoundException, InvalidOrderException {
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new EntityNotFoundException(ordineId, "Ordine"));
        if (ordine.getChiusuraOrdine() != null) {
            throw new InvalidOrderException("Il conto è chiuso, non è possibile aggiungere piatti all'ordine.");
        }
        Prenotazione prenotazione = ordine.getPrenotazione();
        Menu menu = menuService.getMenuByPrenotazioneId(prenotazione.getId());
        List<Piatto> nuoviPiatti = piattiId.stream()
                .map(piattoId -> {
                    try {
                        return piattoRepository.findById(piattoId)
                                .orElseThrow(() -> new EntityNotFoundException(piattoId, "Piatto"));
                    } catch (EntityNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        for (Piatto piatto : nuoviPiatti) {
            if (!menu.getPiatti().contains(piatto)) {
                throw new InvalidOrderException("Il piatto " + piatto.getNome() + " non è presente nel menu selezionato.");
            }
        }
        ordine.getPiatti().addAll(nuoviPiatti);
        ordine.setTotale(ordine.getTotale() + nuoviPiatti.stream().mapToDouble(Piatto::getPrezzo).sum());
        Ordine updatedOrdine = ordineRepository.saveAndFlush(ordine);
        return convertToResponse(updatedOrdine);
    }

    public OrdineResponse chiudiOrdine(Long id) throws EntityNotFoundException {
        Ordine ordine = ordineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ordine"));
        Prenotazione prenotazione = ordine.getPrenotazione();
        Ristorante ristorante = prenotazione.getRistorante();
        Tavolo tavolo = prenotazione.getTavolo();
        ristorante.setPostiDisponibili(Math.min(ristorante.getPostiDisponibili() + prenotazione.getPosti(), ristorante.getPostiTotali()));
        ristoranteRepository.save(ristorante);
        prenotazioneService.liberaPostiPrenotazione(prenotazione.getId());
        tavoloService.liberaTavolo(tavolo.getId());
        ordine.setChiusuraOrdine(LocalDateTime.now());
        ordine.setChiuso(true);
        Ordine updatedOrdine = ordineRepository.saveAndFlush(ordine);
        fileService.writeToFile("src/main/resources/scontrini.txt", convertToResponse(updatedOrdine).toString());
        return convertToResponse(updatedOrdine);
    }

    private OrdineResponse convertToResponse(Ordine ordine) {
        List<String> piattiNomi = ordine.getPiatti().stream()
                .map(Piatto::getNome)
                .collect(Collectors.toList());
        List<Double> piattiPrezzi = ordine.getPiatti().stream()
                .map(Piatto::getPrezzo)
                .collect(Collectors.toList());
        return OrdineResponse.builder()
                .id(ordine.getId())
                .prenotazioneId(ordine.getPrenotazione().getId())
                .piattiNomi(piattiNomi)
                .piattiPrezzi(piattiPrezzi)
                .totale(ordine.getTotale())
                .creazioneOrdine(ordine.getCreazioneOrdine())
                .chiusuraOrdine(ordine.getChiusuraOrdine())
                .build();
    }
}