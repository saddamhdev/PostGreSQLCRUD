package com.PostGreSQL.controller;
import com.PostGreSQL.model.Project;
import com.PostGreSQL.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository repo;

    @PostMapping
    public Project addProject(@RequestBody Project project) {
        return repo.save(project);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return repo.findAll();
    }

    @GetMapping("/{title}")
    public Project getByTitle(@PathVariable String title) {
        return repo.findByTitle(title).orElseThrow(() -> new RuntimeException("Project not found"));
    }

}
