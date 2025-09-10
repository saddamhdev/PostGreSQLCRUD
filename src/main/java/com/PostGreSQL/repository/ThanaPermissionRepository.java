package com.PostGreSQL.repository;

import com.PostGreSQL.model.ThanaPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ThanaPermissionRepository extends JpaRepository<ThanaPermission, String> {
    Optional<ThanaPermission> findByUserIdAndDivisionAndDistrict(Long userId, String division, String district);
     List<ThanaPermission> findByUserId(Long userId);
    // fetch a user’s permissions with all thanas
    @Query("SELECT DISTINCT p FROM ThanaPermission p JOIN FETCH p.thanaNames WHERE p.userId = :userId")
    List<ThanaPermission> findWithThanasByUserId(@Param("userId") Long userId);

    // fetch all permissions with userId + thanas
    @Query("SELECT DISTINCT p FROM ThanaPermission p JOIN FETCH p.thanaNames")
    List<ThanaPermission> findAllWithThanas();
}
