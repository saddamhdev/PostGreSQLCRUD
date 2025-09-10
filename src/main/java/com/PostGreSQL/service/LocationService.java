package com.PostGreSQL.service;

import com.PostGreSQL.model.Location;
import com.PostGreSQL.repository.LocationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocationService {
    @Autowired
    private  LocationRepository locationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void importLocationsFromJson(String fileName) throws IOException {
        // Load from classpath
        ClassPathResource resource = new ClassPathResource(fileName);

        Map<String, Map<String, List<String>>> data =
                objectMapper.readValue(resource.getInputStream(),
                        new TypeReference<>() {});

        for (Map.Entry<String, Map<String, List<String>>> divisionEntry : data.entrySet()) {
            String division = divisionEntry.getKey();
            Map<String, List<String>> districts = divisionEntry.getValue();

            for (Map.Entry<String, List<String>> districtEntry : districts.entrySet()) {
                String district = districtEntry.getKey();
                List<String> upazilas = districtEntry.getValue();

                Location location = new Location(division, district, upazilas);
                locationRepository.save(location);
            }
        }
    }

    public Map<String, Map<String, List<String>>> getAllLocations() {
        List<Location> locations = locationRepository.findAll();

        Map<String, Map<String, List<String>>> result = new HashMap<>();

        for (Location loc : locations) {
            result
                    .computeIfAbsent(loc.getDivision(), k -> new HashMap<>())
                    .put(loc.getDistrict(), loc.getUpazilas());
        }

        return result;
    }
}
