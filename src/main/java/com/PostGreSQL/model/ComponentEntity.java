package com.PostGreSQL.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ComponentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;   // component name
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "page_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PageEntity page;


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "ComponentEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", page=" + page +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PageEntity getPage() {
        return page;
    }
}
