package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Comune;
import com.example.buonAppetito.entities.Proprietario;
import com.example.buonAppetito.entities.Ristorante;
import com.example.buonAppetito.enums.Role;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.ComuneRepository;
import com.example.buonAppetito.repositories.ProprietarioRepository;
import com.example.buonAppetito.repositories.RistoranteRepository;
import com.example.buonAppetito.request.ProprietarioRequest;
import com.example.buonAppetito.response.ProprietarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProprietarioService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private RistoranteRepository ristoranteRepository;

    public ProprietarioResponse getProprietarioById(Long id) throws EntityNotFoundException {
        Proprietario proprietario = proprietarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Proprietario"));
        return convertToResponse(proprietario);
    }

    public List<ProprietarioResponse> getAll() {
        List<Proprietario> proprietari = proprietarioRepository.findAll();
        return proprietari.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public ProprietarioResponse createProprietario(ProprietarioRequest request) throws EntityNotFoundException {
        Comune comune = Optional.ofNullable(comuneRepository.findComuneByNome(request.getNomeComune()))
                .orElseThrow(() -> new EntityNotFoundException(null, "Comune"));

        List<Ristorante> ristoranti = ristoranteRepository.findAllById(request.getIdRistoranti());

        Proprietario proprietario = Proprietario.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .comune(comune)
                .indirizzo(request.getIndirizzo())
                .telefono(request.getTelefono())
                .dataNascita(request.getDataNascita())
                .password(request.getPassword())
                .role(Role.RISTORATORE)
                .ristoranti(ristoranti)
                .build();

        proprietario = proprietarioRepository.saveAndFlush(proprietario);
        return convertToResponse(proprietario);
    }

    public ProprietarioResponse updateProprietario(Long id, ProprietarioRequest updatedRequest) throws EntityNotFoundException {
        Proprietario proprietario = proprietarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Proprietario"));

        Comune comune = Optional.ofNullable(comuneRepository.findComuneByNome(updatedRequest.getNomeComune()))
                .orElseThrow(() -> new EntityNotFoundException(null, "Comune"));

        List<Ristorante> ristoranti = ristoranteRepository.findAllById(updatedRequest.getIdRistoranti());

        Proprietario updatedProprietario = Proprietario.builder()
                .id(proprietario.getId())
                .nome(updatedRequest.getNome())
                .cognome(updatedRequest.getCognome())
                .email(updatedRequest.getEmail())
                .comune(comune)
                .indirizzo(updatedRequest.getIndirizzo())
                .telefono(updatedRequest.getTelefono())
                .dataNascita(updatedRequest.getDataNascita())
                .password(updatedRequest.getPassword())
                .role(proprietario.getRole())
                .ristoranti(ristoranti)
                .build();

        updatedProprietario = proprietarioRepository.saveAndFlush(updatedProprietario);
        return convertToResponse(updatedProprietario);
    }

    public void deleteProprietarioById(Long id) {
        proprietarioRepository.deleteById(id);
    }

    private ProprietarioResponse convertToResponse(Proprietario proprietario) {
        return ProprietarioResponse.builder()
                .id(proprietario.getId())
                .nome(proprietario.getNome())
                .cognome(proprietario.getCognome())
                .email(proprietario.getEmail())
                .nomeComune(proprietario.getComune().getNome())
                .indirizzo(proprietario.getIndirizzo())
                .telefono(proprietario.getTelefono())
                .dataNascita(proprietario.getDataNascita())
                .nomiRistoranti(proprietario.getRistoranti().stream().map(Ristorante::getNome).collect(Collectors.toList()))
                .build();
    }
}