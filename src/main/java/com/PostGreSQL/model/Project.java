package com.PostGreSQL.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")// title usually short
    private String title;

    @Column(columnDefinition = "TEXT") // if your icon is a URL/base64 string, increase limit
    private String icon;

    @Column(length = 255)
    private String role;




    @Column(columnDefinition = "TEXT") // description can be long, use TEXT
    private String description;

    @ElementCollection
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology", length = 255)
    private List<String> technologies = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_features", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "feature", columnDefinition = "TEXT") // features can be long text
    private List<String> features = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_demo_links", joinColumns = @JoinColumn(name = "project_id"))
    private List<DemoLink> demoLinks = new ArrayList<>();

    // Getters and setters

    public Project() {
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<DemoLink> getDemoLinks() {
        return demoLinks;
    }

    public void setDemoLinks(List<DemoLink> demoLinks) {
        this.demoLinks = demoLinks;
    }
}
