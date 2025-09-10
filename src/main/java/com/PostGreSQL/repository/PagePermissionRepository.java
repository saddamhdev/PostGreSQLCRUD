package com.PostGreSQL.repository;

import com.PostGreSQL.model.PagePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagePermissionRepository extends JpaRepository<PagePermission, Long> {
    List<PagePermission> findByUserId(Long userId);
}
