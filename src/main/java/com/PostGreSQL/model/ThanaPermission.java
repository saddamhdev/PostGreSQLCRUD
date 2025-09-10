package com.PostGreSQL.model;


import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "thana_permissions")
public class ThanaPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long userId;
    private String division;
    private String district;
    @ElementCollection
    @CollectionTable(name = "thana_permission_thanas", joinColumns = @JoinColumn(name = "permission_id"))
    @Column(name = "thana")
    private List<String> thanaNames;



    public ThanaPermission() {
    }
// getters & setters


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public List<String> getThanaNames() { return thanaNames; }
    public void setThanaNames(List<String> thanaNames) { this.thanaNames = thanaNames; }

    public ThanaPermission(Long userId, String division, String district, List<String> thanaNames) {
        this.userId = userId;
        this.division = division;
        this.district = district;
        this.thanaNames = thanaNames;
    }

    @Override
    public String toString() {
        return "ThanaPermission{" +
                "id=" + id +
                ", userId=" + userId +
                ", division='" + division + '\'' +
                ", district='" + district + '\'' +
                ", thanaNames=" + thanaNames +
                '}';
    }
}
