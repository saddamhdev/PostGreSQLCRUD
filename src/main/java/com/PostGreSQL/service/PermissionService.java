package com.PostGreSQL.service;

import com.PostGreSQL.model.ThanaPermission;
import com.PostGreSQL.model.User;
import com.PostGreSQL.model.UserPermissionDTO;
import com.PostGreSQL.repository.ThanaPermissionRepository;
import com.PostGreSQL.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final ThanaPermissionRepository thanaRepo;
    private final UserRepository userRepo;

    public PermissionService(ThanaPermissionRepository thanaRepo, UserRepository userRepo) {
        this.thanaRepo = thanaRepo;
        this.userRepo = userRepo;
    }

    // ✅ Get permissions by specific user
    public List<UserPermissionDTO> getPermissionsByUser(Long userId) {
        List<ThanaPermission> perms = thanaRepo.findWithThanasByUserId(userId);

        // Fetch userName from UserRepository
        String userName = userRepo.findById(userId)
                .map(User::getName)
                .orElse("Unknown");

        return perms.stream()
                .map(p -> new UserPermissionDTO(
                        p.getUserId(),
                        userName,
                        p.getDivision(),
                        p.getDistrict(),
                        p.getThanaNames()
                ))
                .collect(Collectors.toList());
    }

    // ✅ Get all permissions with usernames
    public List<UserPermissionDTO> getAllPermissions() {
        List<ThanaPermission> perms = thanaRepo.findAllWithThanas();

        return perms.stream()
                .map(p -> new UserPermissionDTO(
                        p.getUserId(),
                        userRepo.findById(p.getUserId())
                                .map(User::getName)
                                .orElse("Unknown"),
                        p.getDivision(),
                        p.getDistrict(),
                        p.getThanaNames()
                ))
                .collect(Collectors.toList());
    }
}
