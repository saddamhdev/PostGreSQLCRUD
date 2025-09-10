package com.PostGreSQL.controller;


import com.PostGreSQL.model.UserPermissionDTO;
import com.PostGreSQL.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users_permissions")
public class PermissionController {

    private final PermissionService service;

    public PermissionController(PermissionService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<UserPermissionDTO> getPermissionsForUser(@PathVariable Long userId) {
        return service.getPermissionsByUser(userId);
    }

    @GetMapping
    public List<UserPermissionDTO> getAllPermissions() {
        return service.getAllPermissions();
    }
}
