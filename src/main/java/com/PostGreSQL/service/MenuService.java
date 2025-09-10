package com.PostGreSQL.service;

import com.PostGreSQL.model.Menu;
import com.PostGreSQL.repository.MenuRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public void delete(Long id) {
        menuRepository.deleteById(id);
    }
}
