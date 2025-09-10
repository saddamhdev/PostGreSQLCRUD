package com.PostGreSQL.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String division;
    private String district;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "upazilas", joinColumns = @JoinColumn(name = "location_id"))
    @Column(name = "upazila")
    private List<String> upazilas;

    public Location() {}

    public Location(String division, String district, List<String> upazilas) {
        this.division = division;
        this.district = district;
        this.upazilas = upazilas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<String> getUpazilas() {
        return upazilas;
    }

    public void setUpazilas(List<String> upazilas) {
        this.upazilas = upazilas;
    }
// getters & setters
}

