package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Comune;
import com.example.buonAppetito.entities.Menu;
import com.example.buonAppetito.entities.Proprietario;
import com.example.buonAppetito.entities.Ristorante;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.ComuneRepository;
import com.example.buonAppetito.repositories.MenuRepository;
import com.example.buonAppetito.repositories.ProprietarioRepository;
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
    private MenuRepository menuRepository;
    @Autowired
    private ComuneRepository comuneRepository;
    @Autowired
    private ProprietarioRepository proprietarioRepository;

    public RistoranteResponse getRistoranteById(Long id) throws EntityNotFoundException {
        Ristorante ristorante = ristoranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ristorante"));
        return convertToResponse(ristorante);
    }

    public List<RistoranteResponse> getAll() {
        List<Ristorante> ristoranti = ristoranteRepository.findAll();
        return ristoranti.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public RistoranteResponse createRistorannte(RistoranteRequest request) throws EntityNotFoundException {
        Comune comune = comuneRepository.findById(request.getComuneId())
                .orElseThrow(() -> new EntityNotFoundException(request.getComuneId(), "Comune"));
        List<Menu> menuList = menuRepository.findAllById(request.getMenuIds());
        if (menuList.isEmpty()) {
            throw new EntityNotFoundException(404L, "Menu");
        }
        Proprietario proprietario = proprietarioRepository.findById(request.getIdProprietario())
                .orElseThrow(() -> new EntityNotFoundException(request.getIdProprietario(), "Proprietario"));

        Ristorante ristorante = Ristorante.builder()
                .nome(request.getNome())
                .indirizzo(request.getIndirizzo())
                .comune(comune)
                .orarioApertura(request.getOrarioApertura())
                .orarioChiusura(request.getOrarioChiusura())
                .menu(menuList)
                .proprietario(proprietario)
                .build();
        Ristorante savedRistorante = ristoranteRepository.saveAndFlush(ristorante);
        return convertToResponse(savedRistorante);
    }

    public RistoranteResponse updateRistorante(Long id, RistoranteRequest updatedRistoranteRequest) throws EntityNotFoundException {
        Ristorante ristorante = ristoranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Ristorante"));

        Comune comune = comuneRepository.findById(updatedRistoranteRequest.getComuneId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRistoranteRequest.getComuneId(), "Comune"));
        List<Menu> menuList = menuRepository.findAllById(updatedRistoranteRequest.getMenuIds());
        if (menuList.isEmpty()) {
            throw new EntityNotFoundException(404L, "Menu");
        }
        Proprietario proprietario = proprietarioRepository.findById(updatedRistoranteRequest.getIdProprietario())
                .orElseThrow(() -> new EntityNotFoundException(updatedRistoranteRequest.getIdProprietario(), "Proprietario"));

        Ristorante updatedRistorante = Ristorante.builder()
                .id(ristorante.getId())
                .nome(updatedRistoranteRequest.getNome())
                .indirizzo(updatedRistoranteRequest.getIndirizzo())
                .comune(comune)
                .orarioApertura(updatedRistoranteRequest.getOrarioApertura())
                .orarioChiusura(updatedRistoranteRequest.getOrarioChiusura())
                .menu(menuList)
                .proprietario(proprietario)
                .build();
        updatedRistorante = ristoranteRepository.saveAndFlush(updatedRistorante);
        return convertToResponse(updatedRistorante);
    }

    public void deleteRistoranteById(Long id) throws EntityNotFoundException {
        if (!ristoranteRepository.existsById(id)) {
            throw new EntityNotFoundException(id, "Ristorante");
        }
        ristoranteRepository.deleteById(id);
    }

    private RistoranteResponse convertToResponse(Ristorante ristorante) {
        return RistoranteResponse.builder()
                .id(ristorante.getId())
                .nome(ristorante.getNome())
                .indirizzo(ristorante.getIndirizzo())
                .comuneId(ristorante.getComune().getId())
                .comuneNome(ristorante.getComune().getNome())
                .orarioApertura(ristorante.getOrarioApertura())
                .orarioChiusura(ristorante.getOrarioChiusura())
                .menuIds(ristorante.getMenu().stream().map(Menu::getId).collect(Collectors.toList()))
                .idProprietario(ristorante.getProprietario().getId())
                .build();
    }
}