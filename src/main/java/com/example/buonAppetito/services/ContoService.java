package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Conto;
import com.example.buonAppetito.entities.Ordine;
import com.example.buonAppetito.entities.Tavolo;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.ContoRepository;
import com.example.buonAppetito.repositories.OrdineRepository;
import com.example.buonAppetito.repositories.TavoloRepository;
import com.example.buonAppetito.request.ContoRequest;
import com.example.buonAppetito.response.ContoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContoService {

    @Autowired
    private ContoRepository contoRepository;

    @Autowired
    private TavoloRepository tavoloRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    public ContoResponse getContoById(Long id) throws EntityNotFoundException {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Conto"));
        return convertToResponse(conto);
    }

    public List<ContoResponse> getAll() {
        List<Conto> conti = contoRepository.findAll();
        return conti.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public ContoResponse createConto(ContoRequest request) throws EntityNotFoundException {
        Tavolo tavolo = tavoloRepository.findById(request.getTavoloId())
                .orElseThrow(() -> new EntityNotFoundException(request.getTavoloId(), "Tavolo"));

        List<Ordine> ordini = ordineRepository.findAllById(request.getOrdiniId());

        Conto conto = Conto.builder()
                .totale(request.getTotale())
                .tavolo(tavolo)
                .ordini(ordini)
                .timestamp(LocalDateTime.now())
                .build();

        conto = contoRepository.save(conto);
        return convertToResponse(conto);
    }

    public ContoResponse updateConto(Long id, ContoRequest request) throws EntityNotFoundException {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Conto"));

        Tavolo tavolo = tavoloRepository.findById(request.getTavoloId())
                .orElseThrow(() -> new EntityNotFoundException(request.getTavoloId(), "Tavolo"));

        List<Ordine> ordini = ordineRepository.findAllById(request.getOrdiniId());

        conto.setTotale(request.getTotale());
        conto.setTavolo(tavolo);
        conto.setOrdini(ordini);
        conto.setTimestamp(LocalDateTime.now());

        conto = contoRepository.save(conto);
        return convertToResponse(conto);
    }

    public void deleteContoById(Long id) throws EntityNotFoundException {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Conto"));
        contoRepository.delete(conto);
    }

    private ContoResponse convertToResponse(Conto conto) {
        return ContoResponse.builder()
                .id(conto.getId())
                .totale(conto.getTotale())
                .tavoloId(conto.getTavolo().getId())
                .ordini(conto.getOrdini())
                .timestamp(conto.getTimestamp())
                .build();
    }
}