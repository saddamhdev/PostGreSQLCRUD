package com.PostGreSQL.controller;

import com.PostGreSQL.dto.MenuDTO;
import com.PostGreSQL.model.Menu;
import com.PostGreSQL.repository.MenuRepository;
import com.PostGreSQL.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "http://localhost:4200") // allow Angular
public class MenuController {
    @Autowired
    private MenuRepository menuRepository;
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public Menu createMenu(@RequestBody Menu menu) {
        return menuService.save(menu);
    }

    @GetMapping
    public List<MenuDTO> getMenus() {
        return menuService.findAll().stream()
                .map(m -> new MenuDTO(m.getId(), m.getName()))
                .toList();
    }

    // ✅ Delete Menu using POST
    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        if (!menuRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404 if menu not found
        }

        try {
            menuRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception ex) {
            // e.g. foreign key constraint violation
            return ResponseEntity.status(409).body("❌ Cannot delete menu because it has linked pages/components.");
        }
    }
}
