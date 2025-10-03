package com.PostGreSQL.controller;
import com.PostGreSQL.model.Project;
import com.PostGreSQL.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository repo;

    @PostMapping
    public Project addProject(@RequestBody Project project) {
        project.setPosition(repo.findAll().size()+1);
        return repo.save(project);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return repo.findAllByOrderByPositionAsc();
    }

    @GetMapping("/{title}")
    public Project getByTitle(@PathVariable long title) {
        return repo.findById(title).orElseThrow(() -> new RuntimeException("Project not found"));
    }
    // READ by ID (new)
    @GetMapping("/get/{id}")
    public Project getById(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    // UPDATE (new)
    @PostMapping("edit/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        return repo.findById(id).map(project -> {
            project.setTitle(updatedProject.getTitle());
            project.setIcon(updatedProject.getIcon());
            project.setRole(updatedProject.getRole());
            project.setDescription(updatedProject.getDescription());
            project.setTechnologies(updatedProject.getTechnologies());
            project.setFeatures(updatedProject.getFeatures());
            project.setDemoLinks(updatedProject.getDemoLinks());
            return repo.save(project);
        }).orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    // DELETE (new)
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Project not found with id " + id);
        }
        repo.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Project deleted successfully!");
        return ResponseEntity.ok(response);
    }

}
