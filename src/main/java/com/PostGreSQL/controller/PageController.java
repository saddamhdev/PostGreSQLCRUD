package com.PostGreSQL.controller;

import com.PostGreSQL.dto.PageDTO;
import com.PostGreSQL.model.PageEntity;
import com.PostGreSQL.repository.PageRepository;
import com.PostGreSQL.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pages")
@CrossOrigin(origins = "http://localhost:4200")
public class PageController {
    private final PageService pageService;

    @Autowired
    private PageRepository pageRepository;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @PostMapping
    public PageEntity createPage(@RequestBody PageEntity page) {
        return pageService.save(page);
    }

    @GetMapping
    public List<PageDTO> getPages() {
        return pageService.findAll().stream()
                .map(p -> new PageDTO(
                        p.getId(),
                        p.getName(),
                        p.getMenu() != null ? p.getMenu().getName() : null
                ))
                .toList();
    }

    // ✅ Delete Page using POST
    // ✅ Delete Page using POST
    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deletePage(@PathVariable Long id) {
        try {
            if (!pageRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ Page not found");
            }

            pageRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204

        } catch (DataIntegrityViolationException ex) {
            // Happens when foreign key constraint is violated
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("❌ Cannot delete page because it has linked components.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Unexpected error: " + ex.getMessage());
        }
    }
}
