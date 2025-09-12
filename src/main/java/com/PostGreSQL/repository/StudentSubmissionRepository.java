package com.PostGreSQL.repository;

import com.PostGreSQL.model.StudentSubmission;
import com.PostGreSQL.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentSubmissionRepository extends JpaRepository<StudentSubmission, Long> {
    Optional<User> findByEmail(String email);
    Page<StudentSubmission> findByStatusAndInformationType(String status,String informationType,Pageable pageable);
    Page<StudentSubmission> findByDivisionAndDistrictAndUpazilaInAndStatusAndInformationType(
            String division,
            String district,
            List<String> upazilas,
            String status,
            String informationType,
            Pageable pageable
    );

    Optional<StudentSubmission> findBySscRoll(String sscRoll);
    Optional<StudentSubmission> findBySscRollAndStatus(String sscRoll,String Status);
    List<StudentSubmission> findByStatus(String status);

}
