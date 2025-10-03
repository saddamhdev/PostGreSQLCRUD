package com.PostGreSQL.repository;
import com.PostGreSQL.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByTitle(String title);
    List<Project> findAllByOrderByPositionAsc();  // ✅ Spring Data JPA method
}
