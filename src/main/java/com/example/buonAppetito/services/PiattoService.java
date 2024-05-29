package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Piatto;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.PiattoRepository;
import com.example.buonAppetito.request.PiattoRequest;
import com.example.buonAppetito.response.PiattoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PiattoService {

    @Autowired
    private PiattoRepository piattoRepository;

    public PiattoResponse getPiattoById(Long id) throws EntityNotFoundException {
        Optional<Piatto> piattoOptional = piattoRepository.findById(id);
        return piattoOptional.map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException(id, "Piatto"));
    }

    public List<PiattoResponse> getAll() {
        List<Piatto> piatti = piattoRepository.findAll();
        return piatti.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public PiattoResponse createPiatto(PiattoRequest request) {
        Piatto piatto = Piatto.builder()
                .nome(request.getNome())
                .build();
        Piatto savedPiatto = piattoRepository.saveAndFlush(piatto);
        return convertToResponse(savedPiatto);
    }

    public PiattoResponse updatePiatto(Long id, PiattoRequest updatedPiattoRequest) throws EntityNotFoundException {
        Optional<Piatto> piattoOptional = piattoRepository.findById(id);
        if(piattoOptional.isPresent()) {
            Piatto piatto = piattoOptional.get();
            Piatto updatedPiatto = Piatto.builder()
                    .id(piatto.getId())
                    .nome(updatedPiattoRequest.getNome())
                    .build();
            updatedPiatto = piattoRepository.saveAndFlush(updatedPiatto);
            return convertToResponse(updatedPiatto);
        } else {
            throw new EntityNotFoundException(id, "Piatto");
        }
    }

    public void deletePiattoById(Long id) throws EntityNotFoundException {
        if(piattoRepository.existsById(id)) {
            piattoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, "Piatto");
        }
    }

    private PiattoResponse convertToResponse(Piatto piatto) {
        return PiattoResponse.builder()
                .id(piatto.getId())
                .nome(piatto.getNome())
                .build();
    }
}