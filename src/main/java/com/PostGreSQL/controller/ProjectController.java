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
            project.setDescription(updatedProject.getDescription());
            project.setTechnologies(updatedProject.getTechnologies());
            project.setFeatures(updatedProject.getFeatures());
            project.setDemoLinks(updatedProject.getDemoLinks());
            return repo.save(project);
        }).orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    // DELETE (new)
    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Project not found with id " + id);
        }
        repo.deleteById(id);
        return "✅ Project deleted successfully!";
    }

}
