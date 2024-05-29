package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Conto;
import com.example.buonAppetito.entities.Ordine;
import com.example.buonAppetito.entities.Piatto;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.ContoRepository;
import com.example.buonAppetito.repositories.OrdineRepository;
import com.example.buonAppetito.repositories.PiattoRepository;
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
    private ContoRepository contoRepository;

    @Autowired
    private PiattoRepository piattoRepository;

    public OrdineResponse getOrdineById(Long id) throws EntityNotFoundException {
        Ordine ordine = ordineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ordine"));
        return convertToResponse(ordine);
    }

    public List<OrdineResponse> getAll() {
        List<Ordine> ordini = ordineRepository.findAll();
        return ordini.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public OrdineResponse createOrdine(OrdineRequest request) throws EntityNotFoundException {
        Conto conto = contoRepository.findById(request.getContoId())
                .orElseThrow(() -> new EntityNotFoundException(request.getContoId(), "Conto"));
        Piatto piatto = piattoRepository.findById(request.getPiattoId())
                .orElseThrow(() -> new EntityNotFoundException(request.getPiattoId(), "Piatto"));

        Ordine ordine = Ordine.builder()
                .conto(conto)
                .piatto(piatto)
                .quantita(request.getQuantita())
                .timestamp(LocalDateTime.now())
                .build();

        ordine = ordineRepository.saveAndFlush(ordine);
        return convertToResponse(ordine);
    }

    public OrdineResponse updateOrdine(Long id, OrdineRequest updatedRequest) throws EntityNotFoundException {
        Ordine existingOrdine = ordineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ordine"));

        Conto conto = contoRepository.findById(updatedRequest.getContoId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getContoId(), "Conto"));
        Piatto piatto = piattoRepository.findById(updatedRequest.getPiattoId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getPiattoId(), "Piatto"));

        existingOrdine = Ordine.builder()
                .id(existingOrdine.getId())
                .conto(conto)
                .piatto(piatto)
                .quantita(updatedRequest.getQuantita())
                .timestamp(existingOrdine.getTimestamp())
                .build();

        existingOrdine = ordineRepository.saveAndFlush(existingOrdine);
        return convertToResponse(existingOrdine);
    }

    public void deleteOrdineById(Long id) throws EntityNotFoundException {
        if (!ordineRepository.existsById(id)) {
            throw new EntityNotFoundException(id, "Ordine");
        }
        ordineRepository.deleteById(id);
    }

    private OrdineResponse convertToResponse(Ordine ordine) {
        return OrdineResponse.builder()
                .id(ordine.getId())
                .contoId(ordine.getConto().getId())
                .nomePiatto(ordine.getPiatto().getNome())
                .quantita(ordine.getQuantita())
                .timestamp(ordine.getTimestamp())
                .build();
    }
}
