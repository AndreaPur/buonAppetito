package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Comune;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.ComuneRepository;
import com.example.buonAppetito.request.ComuneRequest;
import com.example.buonAppetito.response.ComuneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComuneService {

    @Autowired
    private ComuneRepository comuneRepository;

    public ComuneResponse getComuneById(Long id) throws EntityNotFoundException {
        Optional<Comune> comuneOptional = comuneRepository.findById(id);
        if (comuneOptional.isPresent()) {
            return convertToResponse(comuneOptional.get());
        } else {
            throw new EntityNotFoundException(id, "Comune");
        }
    }

    public List<ComuneResponse> getAll() {
        List<Comune> comuni = comuneRepository.findAll();
        return comuni.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public ComuneResponse createComune(ComuneRequest request) {
        Comune comune = Comune.builder()
                .nome(request.getNome())
                .build();
        Comune savedComune = comuneRepository.saveAndFlush(comune);
        return convertToResponse(savedComune);
    }

    public ComuneResponse updateComune(Long id, ComuneRequest updatedComuneRequest) throws EntityNotFoundException {
        Optional<Comune> comuneOptional = comuneRepository.findById(id);
        if (comuneOptional.isPresent()) {
            Comune comune = comuneOptional.get();
            comune.setNome(updatedComuneRequest.getNome());
            comune = comuneRepository.saveAndFlush(comune);
            return convertToResponse(comune);
        } else {
            throw new EntityNotFoundException(id, "Comune");
        }
    }

    public void deleteComuneById(Long id) throws EntityNotFoundException {
        Optional<Comune> comuneOptional = comuneRepository.findById(id);
        if (comuneOptional.isPresent()) {
            comuneRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, "Comune");
        }
    }

    private ComuneResponse convertToResponse(Comune comune) {
        return ComuneResponse.builder()
                .id(comune.getId())
                .nome(comune.getNome())
                .build();
    }
}
