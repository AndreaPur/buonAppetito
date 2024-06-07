package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Piatto;
import com.example.buonAppetito.entities.Menu;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.MenuRepository;
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

    @Autowired
    private MenuRepository menuRepository;

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
                .prezzo(request.getPrezzo())
                .build();
        Piatto savedPiatto = piattoRepository.saveAndFlush(piatto);
        return convertToResponse(savedPiatto);
    }

    public PiattoResponse addPiattoToMenu(Long piattoId, Long menuId) throws EntityNotFoundException {
        Piatto piatto = piattoRepository.findById(piattoId)
                .orElseThrow(() -> new EntityNotFoundException(piattoId, "Piatto"));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException(menuId, "Menu"));

        piatto.setMenu(menu);
        Piatto updatedPiatto = piattoRepository.saveAndFlush(piatto);
        return convertToResponse(updatedPiatto);
    }

    public PiattoResponse updatePiatto(Long id, PiattoRequest updatedRequest) throws EntityNotFoundException {
        Piatto piatto = piattoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Piatto"));

        piatto.setNome(updatedRequest.getNome());
        piatto.setPrezzo(updatedRequest.getPrezzo());

        Piatto updatedPiatto = piattoRepository.saveAndFlush(piatto);
        return convertToResponse(updatedPiatto);
    }

    public void deletePiattoById(Long id) throws EntityNotFoundException {
        if (!piattoRepository.existsById(id)) {
            throw new EntityNotFoundException(id, "Piatto");
        }
        piattoRepository.deleteById(id);
    }

    private PiattoResponse convertToResponse(Piatto piatto) {
        return PiattoResponse.builder()
                .id(piatto.getId())
                .nome(piatto.getNome())
                .prezzo(piatto.getPrezzo())
                .build();
    }
}
