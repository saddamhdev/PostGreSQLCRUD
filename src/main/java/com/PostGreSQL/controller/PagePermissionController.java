package com.PostGreSQL.controller;

import com.PostGreSQL.dto.TreePermissionRequest;
import com.PostGreSQL.dto.TreePermissionResponse;
import com.PostGreSQL.service.PagePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagePermissions")
@RequiredArgsConstructor
public class PagePermissionController {
    @Autowired
    private  PagePermissionService permissionService;

    @PostMapping("/tree")
    public ResponseEntity<Void> assignTree(@RequestBody TreePermissionRequest request) {
        permissionService.assignTree(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/tree")
    public TreePermissionResponse getUserPermissionsAsTree(@PathVariable Long userId) {
        System.out.println(userId);
        return permissionService.getPermissionsAsTree(userId);
    }

    // ✅ New endpoint: all users tree
    @GetMapping("/user/all/tree")
    public ResponseEntity<List<TreePermissionResponse>> getAllPermissionsAsTree() {
        return ResponseEntity.ok(permissionService.getAllPermissionsAsTree());
    }
}
