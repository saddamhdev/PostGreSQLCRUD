package com.PostGreSQL.service;
import com.PostGreSQL.dto.*;
import com.PostGreSQL.model.PagePermission;
import com.PostGreSQL.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagePermissionService {
    @Autowired
    private  PagePermissionRepository pagePermissionRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  MenuRepository menuRepository;
    @Autowired
    private  PageRepository pageRepository;
    @Autowired
    private ComponentRepository componentRepository;


    @Transactional(readOnly = true)
    public TreePermissionResponse getPermissionsAsTree(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with id=" + userId
                ));


        // Assigned permissions (ensure not null)
       var assigned = Optional.ofNullable(pagePermissionRepository.findByUserId(userId))
             .orElse(List.of());
       // System.out.println("Saddam "+assigned);
        // Pre-load everything (ensure not null)
        var allMenus = Optional.ofNullable(menuRepository.findAll()).orElse(List.of());
        var allPages = Optional.ofNullable(pageRepository.findAll()).orElse(List.of());
        var allComponents = Optional.ofNullable(componentRepository.findAll()).orElse(List.of());

        // Build tree
        var menuSelections = allMenus.stream().map(menu -> {
            boolean menuChecked = assigned.stream()
                    .anyMatch(p -> p.getMenu() != null && p.getMenu().getId().equals(menu.getId()));

            var pageSelections = allPages.stream()
                    .filter(page -> page.getMenu() != null && page.getMenu().getId().equals(menu.getId()))
                    .map(page -> {
                        boolean pageChecked = assigned.stream()
                                .anyMatch(p -> p.getPage() != null && p.getPage().getId().equals(page.getId()));

                        var componentSelections = allComponents.stream()
                                .filter(comp -> comp.getPage() != null && comp.getPage().getId().equals(page.getId()))
                                .map(comp -> {
                                    boolean compChecked = assigned.stream()
                                            .anyMatch(p -> p.getComponent() != null && p.getComponent().getId().equals(comp.getId()));
                                    return new ComponentSelection(
                                            comp.getId(),
                                            comp.getName() != null ? comp.getName() : "(Unnamed Component)",
                                            compChecked
                                    );
                                })
                                .toList();

                        return new PageSelection(
                                page.getId(),

                                page.getName() != null ? page.getName() : "(Unnamed Page)",
                                pageChecked,
                                componentSelections
                        );
                    })
                    .toList();

            return new MenuSelection(
                    menu.getId(),
                    menu.getName() != null ? menu.getName() : "(Unnamed Menu)",
                    menuChecked,
                    pageSelections
            );
        }).toList();

        return new TreePermissionResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                menuSelections);
    }

    @Transactional(readOnly = true)
    public List<TreePermissionResponse> getAllPermissionsAsTree() {
        return userRepository.findAll().stream()
                .map(user -> getPermissionsAsTree(user.getId())) // reuse existing tree builder
                .collect(Collectors.toList());
    }
    @Transactional
    public void assignTree(TreePermissionRequest request) {
        var user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.userId()));

        // 🧹 Clear old permissions first
        pagePermissionRepository.deleteAll(pagePermissionRepository.findByUserId(request.userId()));

        for (var menuSel : request.menus()) {
            if (menuSel.checked()) {
                var menu = menuRepository.findById(menuSel.id())
                        .orElseThrow(() -> new IllegalArgumentException("Menu not found: " + menuSel.id()));
                pagePermissionRepository.save(new PagePermission(null, user, menu, null, null));
            }

            for (var pageSel : menuSel.pages()) {
                if (pageSel.checked()) {
                    var page = pageRepository.findById(pageSel.id())
                            .orElseThrow(() -> new IllegalArgumentException("Page not found: " + pageSel.id()));
                    pagePermissionRepository.save(new PagePermission(null, user, null, page, null));
                }

                for (var compSel : pageSel.components()) {
                    if (compSel.checked()) {
                        var comp = componentRepository.findById(compSel.id())
                                .orElseThrow(() -> new IllegalArgumentException("Component not found: " + compSel.id()));
                        pagePermissionRepository.save(new PagePermission(null, user, null, null, comp));
                    }
                }
            }
        }
    }
}
