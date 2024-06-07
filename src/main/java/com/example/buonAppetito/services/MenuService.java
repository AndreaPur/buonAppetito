package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Menu;
import com.example.buonAppetito.entities.Piatto;
import com.example.buonAppetito.entities.Prenotazione;
import com.example.buonAppetito.entities.Ristorante;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.MenuRepository;
import com.example.buonAppetito.repositories.PiattoRepository;
import com.example.buonAppetito.repositories.PrenotazioneRepository;
import com.example.buonAppetito.repositories.RistoranteRepository;
import com.example.buonAppetito.request.MenuRequest;
import com.example.buonAppetito.response.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private PiattoRepository piattoRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public MenuResponse getMenuById(Long id) throws EntityNotFoundException {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Menu"));
        return convertToResponse(menu);
    }

    public List<MenuResponse> getAllMenu() {
        List<Menu> menuList = menuRepository.findAll();
        return menuList.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public MenuResponse createMenu(MenuRequest request) throws EntityNotFoundException {
        Ristorante ristorante = ristoranteRepository.findById(request.getRistoranteId())
                .orElseThrow(() -> new EntityNotFoundException(request.getRistoranteId(), "Ristorante"));
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
        Menu menu = Menu.builder()
                .nome(request.getNome())
                .ristorante(ristorante)
                .piatti(piatti)
                .build();
        piatti.forEach(piatto -> piatto.setMenu(menu));
        Menu savedMenu = menuRepository.saveAndFlush(menu);
        return convertToResponse(savedMenu);
    }

    public MenuResponse updateMenu(Long id, MenuRequest updatedRequest) throws EntityNotFoundException {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Menu"));
        Ristorante ristorante = ristoranteRepository.findById(updatedRequest.getRistoranteId())
                .orElseThrow(() -> new EntityNotFoundException(updatedRequest.getRistoranteId(), "Ristorante"));
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
        menu.getPiatti().forEach(piatto -> piatto.setMenu(null));
        menu.getPiatti().clear();
        menu.setNome(updatedRequest.getNome());
        menu.setRistorante(ristorante);
        menu.setPiatti(piatti);
        piatti.forEach(piatto -> piatto.setMenu(menu));
        Menu updatedMenu = menuRepository.saveAndFlush(menu);
        return convertToResponse(updatedMenu);
    }

    public void deleteMenuById(Long id) throws EntityNotFoundException {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException(id, "Menu");
        }
        menuRepository.deleteById(id);
    }

    public Menu getMenuByPrenotazioneId(Long prenotazioneId) throws EntityNotFoundException {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new EntityNotFoundException(prenotazioneId, "Prenotazione"));
        Ristorante ristorante = prenotazione.getRistorante();
        if (ristorante == null) {
            throw new EntityNotFoundException(prenotazioneId, "Il ristorante associato alla prenotazione con ID risulta ");
        }
        List<Menu> menus = ristorante.getMenus();
        if (menus.isEmpty()) {
            throw new EntityNotFoundException(prenotazioneId, "Il menu per il ristorante associato risulta");
        }
        Menu menu = menus.get(0);
        return menu;
    }

    private MenuResponse convertToResponse(Menu menu) {
        List<Long> piattiId = menu.getPiatti().stream()
                .map(Piatto::getId)
                .collect(Collectors.toList());
        List<String> piattiNomi = menu.getPiatti().stream()
                .map(Piatto::getNome)
                .collect(Collectors.toList());
        List<Double> piattiPrezzi = menu.getPiatti().stream()
                .map(Piatto::getPrezzo)
                .collect(Collectors.toList());
        return MenuResponse.builder()
                .id(menu.getId())
                .nome(menu.getNome())
                .piattiId(piattiId)
                .piattiNomi(piattiNomi)
                .piattiPrezzi(piattiPrezzi)
                .nomeRistorante(menu.getRistorante().getNome())
                .build();
    }
}