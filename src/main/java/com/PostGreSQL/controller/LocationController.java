package com.PostGreSQL.controller;


import com.PostGreSQL.service.LocationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "http://localhost:4200") // allow Angular calls

public class LocationController {
    @Autowired
    private LocationService locationService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<Map<String, Map<String, List<String>>>> getLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }
}
