package com.PostGreSQL.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
public class ImageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batch;
    private String status;
    private String fileName;
    private String filePath;   // store relative URL (e.g., /uploads/abc.jpg)

    private LocalDateTime uploadedAt = LocalDateTime.now();

    public ImageRecord() {}

    public ImageRecord(String batch, String status, String fileName, String filePath) {
        this.batch = batch;
        this.status = status;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadedAt = LocalDateTime.now();
    }

    // getters & setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
