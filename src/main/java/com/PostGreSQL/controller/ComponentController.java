package com.PostGreSQL.controller;


import com.PostGreSQL.dto.ComponentDTO;
import com.PostGreSQL.model.ComponentEntity;
import com.PostGreSQL.model.PageEntity;
import com.PostGreSQL.repository.ComponentRepository;
import com.PostGreSQL.repository.PageRepository;
import com.PostGreSQL.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/components")
public class ComponentController {
    private final ComponentService componentService;
    private final PageRepository pageRepository;
    @Autowired
    private ComponentRepository componentRepository;
    public ComponentController(ComponentService componentService, PageRepository pageRepository) {
        this.componentService = componentService;
        this.pageRepository = pageRepository;
    }

    @PostMapping
    public ComponentEntity createComponent(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        Long pageId = Long.valueOf(payload.get("pageId").toString());

        PageEntity page = pageRepository.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        ComponentEntity component = new ComponentEntity();
        component.setName(name);
        component.setPage(page);

        return componentService.save(component);
    }

    @GetMapping
    public List<ComponentDTO> getComponents() {
        return componentService.findAll().stream()
                .map(c -> new ComponentDTO(
                        c.getId(),
                        c.getName(),
                        c.getPage() != null ? c.getPage().getName() : null,
                        (c.getPage() != null && c.getPage().getMenu() != null)
                                ? c.getPage().getMenu().getName()
                                : null
                ))
                .toList();
    }


    // ✅ Delete Component using POST
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteComponent(@PathVariable Long id) {
        System.out.println(id);
        if (!componentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        componentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
