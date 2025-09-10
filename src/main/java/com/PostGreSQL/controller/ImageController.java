package com.PostGreSQL.controller;

import com.PostGreSQL.model.ImageRecord;
import com.PostGreSQL.repository.ImageRepository;
import com.PostGreSQL.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/image")
@CrossOrigin(origins = "http://localhost:4200") // allow Angular calls

public class ImageController {

    // Directory inside Spring Boot project
// Save inside project root
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
// user.dir = your project root


    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LocationService locationService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("batch") String batch,
            @RequestParam("status") String status,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        Map<String, String> response = new HashMap<>();

        try {
          //  locationService.importLocationsFromJson("location.json");
            // Ensure directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
             System.out.println(uploadDir.getPath());
            // Generate unique filename
            String fileName = UUID.randomUUID() + "_" + Objects.requireNonNull(imageFile.getOriginalFilename());
            File dest = new File(UPLOAD_DIR + fileName);

            // Save file
            imageFile.transferTo(dest);

            // Save metadata in DB
            String fileUrl = "/uploads/" + fileName; // accessible via static resources
            ImageRecord record = new ImageRecord(batch, status, fileName, fileUrl);
            imageRepository.save(record);

            // Response
            response.put("message", "✅ File uploaded successfully!");
            response.put("fileName", fileName);
            response.put("fileUrl", fileUrl);
            response.put("batch", batch);
            response.put("status", status);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "❌ Failed to upload file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ Get all uploaded images
    @GetMapping("/images")
    public List<ImageRecord> getAllImages() {
        return imageRepository.findAllByOrderByUploadedAtDesc();
    }


    @PostMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestBody Map<String, String> body) {
        Long id = Long.valueOf(body.get("id"));
        if (id == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "❌ Image ID is required"));
        }

        return imageRepository.findById(id).map(record -> {
            try {
                // Delete physical file
                File file = new File(System.getProperty("user.dir") + "/uploads/" + record.getFileName());
                if (file.exists()) file.delete();

                // Delete from DB
                imageRepository.delete(record);

                return ResponseEntity.ok(Map.of("message", "✅ Image deleted successfully!"));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "❌ Failed to delete: " + e.getMessage()));
            }
        }).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "❌ Image not found"))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<ImageRecord> updateImage(@RequestBody Map<String, String> updates) {
        Long id = Long.valueOf(updates.get("id"));
        if (id == null) {
            return ResponseEntity.badRequest().build(); // no Map
        }

        return imageRepository.findById(id)
                .map(record -> {
                    if (updates.containsKey("batch")) {
                        record.setBatch(updates.get("batch"));
                    }
                    if (updates.containsKey("status")) {
                        record.setStatus(updates.get("status"));
                    }
                    ImageRecord saved = imageRepository.save(record);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
