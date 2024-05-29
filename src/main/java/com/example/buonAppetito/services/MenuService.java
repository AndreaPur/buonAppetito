package com.example.buonAppetito.services;

import com.example.buonAppetito.entities.Menu;
import com.example.buonAppetito.entities.Piatto;
import com.example.buonAppetito.exceptions.EntityNotFoundException;
import com.example.buonAppetito.repositories.MenuRepository;
import com.example.buonAppetito.repositories.PiattoRepository;
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
        List<Piatto> piattiList = request.getIdPiatti().stream()
                .map(id -> {
                    try {
                        return piattoRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(id, "Piatto"));
                    } catch (EntityNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        Menu menu = Menu.builder()
                .nome(request.getNome())
                .piattiList(piattiList)
                .build();

        Menu savedMenu = menuRepository.saveAndFlush(menu);
        return convertToResponse(savedMenu);
    }

    public MenuResponse updateMenu(Long id, MenuRequest updatedRequest) throws EntityNotFoundException {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Menu"));

        List<Piatto> piattiList = updatedRequest.getIdPiatti().stream()
                .map(piattoId -> {
                    try {
                        return piattoRepository.findById(piattoId)
                                .orElseThrow(() -> new EntityNotFoundException(piattoId, "Piatto"));
                    } catch (EntityNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        menu.setNome(updatedRequest.getNome());
        menu.setPiattiList(piattiList);

        Menu updatedMenu = menuRepository.saveAndFlush(menu);
        return convertToResponse(updatedMenu);
    }

    public void deleteMenuById(Long id) throws EntityNotFoundException {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException(id, "Menu");
        }
        menuRepository.deleteById(id);
    }

    private MenuResponse convertToResponse(Menu menu) {
        List<String> nomiPiatti = menu.getPiattiList().stream()
                .map(Piatto::getNome)
                .collect(Collectors.toList());

        return MenuResponse.builder()
                .id(menu.getId())
                .nome(menu.getNome())
                .nomiPiatti(nomiPiatti)
                .build();
    }
}