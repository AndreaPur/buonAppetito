package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Comune;
import com.example.buonAppetito.entities.Ristorante;
import com.example.buonAppetito.entities.Menu;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.ComuneRepository;
import com.example.buonAppetito.repositories.RistoranteRepository;
import com.example.buonAppetito.request.RistoranteRequest;
import com.example.buonAppetito.response.RistoranteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RistoranteService {

    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private ComuneRepository comuneRepository;

    public RistoranteResponse getRistoranteById(Long id) throws EntityNotFoundException {
        Ristorante ristorante = ristoranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ristorante"));
        return convertToResponse(ristorante);
    }

    public List<RistoranteResponse> getAll() {
        List<Ristorante> ristoranti = ristoranteRepository.findAll();
        return ristoranti.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public RistoranteResponse createRistorante(RistoranteRequest request) throws EntityNotFoundException {
        Comune comune = comuneRepository.findById(request.getComuneId())
                .orElseThrow(() -> new EntityNotFoundException(request.getComuneId(), "Comune"));

        Ristorante ristorante = Ristorante.builder()
                .nome(request.getNome())
                .indirizzo(request.getIndirizzo())
                .postiTotali(request.getPostiTotali())
                .postiDisponibili(request.getPostiTotali())
                .comune(comune)
                .orarioApertura(request.getOrarioApertura())
                .orarioChiusura(request.getOrarioChiusura())
                .build();

        ristorante = ristoranteRepository.saveAndFlush(ristorante);
        return convertToResponse(ristorante);
    }

    public RistoranteResponse updateRistorante(Long id, RistoranteRequest updatedRequest) throws EntityNotFoundException {
        Ristorante ristorante = ristoranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ristorante"));
        Comune comune = comuneRepository.findById(updatedRequest.getComuneId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getComuneId(), "Comune"));

        ristorante.setNome(updatedRequest.getNome());
        ristorante.setIndirizzo(updatedRequest.getIndirizzo());
        ristorante.setPostiTotali(updatedRequest.getPostiTotali());
        ristorante.setPostiDisponibili(updatedRequest.getPostiTotali());
        ristorante.setComune(comune);
        ristorante.setOrarioApertura(updatedRequest.getOrarioApertura());
        ristorante.setOrarioChiusura(updatedRequest.getOrarioChiusura());

        ristorante = ristoranteRepository.saveAndFlush(ristorante);
        return convertToResponse(ristorante);
    }

    public void deleteRistoranteById(Long id) throws EntityNotFoundException {
        Ristorante ristorante = ristoranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ristorante"));
        ristoranteRepository.deleteById(id);
    }

    private RistoranteResponse convertToResponse(Ristorante ristorante) {
        return RistoranteResponse.builder()
                .id(ristorante.getId())
                .nome(ristorante.getNome())
                .indirizzo(ristorante.getIndirizzo())
                .comuneNome(ristorante.getComune().getNome())
                .postiTotali(ristorante.getPostiTotali())
                .postiDisponibili(ristorante.getPostiDisponibili())
                .orarioApertura(ristorante.getOrarioApertura())
                .orarioChiusura(ristorante.getOrarioChiusura())
                .menuId(ristorante.getMenus() != null ? ristorante.getMenus().stream().map(Menu::getId).collect(Collectors.toList()) : null)
                .build();
    }
}