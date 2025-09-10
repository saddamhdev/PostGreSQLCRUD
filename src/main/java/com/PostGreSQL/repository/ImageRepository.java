package com.PostGreSQL.repository;

import com.PostGreSQL.model.ImageRecord;
import com.PostGreSQL.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageRecord, Long> {
    List<ImageRecord> findAllByOrderByUploadedAtDesc();

}
