package com.PostGreSQL.controller;

import com.PostGreSQL.model.ThanaPermission;
import com.PostGreSQL.repository.ThanaPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/thana")
public class ThanaPermissionController {

    @Autowired
    private ThanaPermissionRepository thanaPermissionRepository;

    @PostMapping("/{userId}/permissions")
    public ResponseEntity<Map<String, String>> savePermissions(
            @PathVariable Long userId,
            @RequestBody ThanaPermission request) {

        // Save new record or update existing
        ThanaPermission permission = thanaPermissionRepository
                .findByUserIdAndDivisionAndDistrict(userId, request.getDivision(), request.getDistrict())
                .orElse(new ThanaPermission());

        permission.setUserId(userId);
        permission.setDivision(request.getDivision());
        permission.setDistrict(request.getDistrict());
        permission.setThanaNames(request.getThanaNames());

        thanaPermissionRepository.save(permission);

        Map<String, String> response = new HashMap<>();
        response.put("message", "✅ Permissions saved/updated for user " + userId);
        return ResponseEntity.ok(response);
    }

    // ✅ fetch existing permissions for a user + district
    @GetMapping("/{userId}/permissions")
    public ResponseEntity<ThanaPermission> getPermissions(
            @PathVariable Long userId,
            @RequestParam String division,
            @RequestParam String district) {

        return thanaPermissionRepository.findByUserIdAndDivisionAndDistrict(userId, division, district)
                .map(p -> ResponseEntity.ok(
                        new ThanaPermission(p.getUserId(), p.getDivision(), p.getDistrict(), p.getThanaNames())))
                .orElse(ResponseEntity.ok(null));
    }
}
