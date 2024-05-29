package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Ristorante;
import com.example.buonAppetito.entities.Tavolo;
import com.example.buonAppetito.repositories.RistoranteRepository;
import com.example.buonAppetito.repositories.TavoloRepository;
import com.example.buonAppetito.request.TavoloRequest;
import com.example.buonAppetito.response.TavoloResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TavoloService {

    @Autowired
    private TavoloRepository tavoloRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;

    public TavoloResponse getTavoloById(Long id){
        Optional<Tavolo> tavoloOptional = tavoloRepository.findById(id);
        return tavoloOptional.map(this::convertToResponse).orElse(null);
    }

    public List<TavoloResponse> getAll(){
        List<Tavolo> tavoli = tavoloRepository.findAll();
        return tavoli.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public TavoloResponse createTavolo(TavoloRequest request){
        Ristorante ristorante = ristoranteRepository.findById(request.getIdRistorante()).orElse(null);
        if(ristorante != null) {
            Tavolo tavolo = Tavolo.builder()
                    .ristorante(ristorante)
                    .posti(request.getPosti())
                    .build();
            tavolo = tavoloRepository.saveAndFlush(tavolo);
            return convertToResponse(tavolo);
        }
        return null;
    }

    public TavoloResponse updateTavolo(Long id, TavoloRequest updatedRequest){
        Optional<Tavolo> tavoloOptional = tavoloRepository.findById(id);
        if(tavoloOptional.isPresent()){
            Tavolo tavolo = tavoloOptional.get();
            Ristorante ristorante = ristoranteRepository.findById(updatedRequest.getIdRistorante()).orElse(null);
            if(ristorante != null) {
                Tavolo updatedTavolo = Tavolo.builder()
                        .ristorante(ristorante)
                        .posti(updatedRequest.getPosti())
                        .build();
                tavolo = tavoloRepository.saveAndFlush(tavolo);
                return convertToResponse(tavolo);
            }
        }
        return null;
    }

    public void deleteTavoloById(Long id) {tavoloRepository.deleteById(id);}

    public TavoloResponse convertToResponse(Tavolo tavolo){
        return TavoloResponse.builder()
                .id(tavolo.getId())
                .posti(tavolo.getPosti())
                .nomeRistorante(tavolo.getRistorante().getNome())
                .build();
    }
}
