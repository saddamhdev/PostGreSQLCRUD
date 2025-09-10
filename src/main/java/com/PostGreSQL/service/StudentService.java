package com.PostGreSQL.service;
import com.PostGreSQL.model.StudentSubmission;
import com.PostGreSQL.model.ThanaPermission;
import com.PostGreSQL.repository.StudentSubmissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentSubmissionRepository repo;

    public StudentService(StudentSubmissionRepository repo) {
        this.repo = repo;
    }

    public Page<StudentSubmission> getStudents(String status,String type ,Pageable pageable) {
        return repo.findByStatusAndInformationType(status,type,pageable);
    }
    public Page<StudentSubmission> getStudentsByPermissionAndStatusAndInformationType(
            ThanaPermission tp, String status,String type, Pageable pageable) {
        return repo.findByDivisionAndDistrictAndUpazilaInAndStatusAndInformationType(
                tp.getDivision(),
                tp.getDistrict(),
                tp.getThanaNames(),
                status,
                type,
                pageable
        );
    }
}
