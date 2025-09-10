package com.PostGreSQL.controller;// src/main/java/com/example/student/StudentInsertController.java



import com.PostGreSQL.exception.BadRequestException2;
import com.PostGreSQL.exception.NotFoundException;
import com.PostGreSQL.model.StudentSubmission;
import com.PostGreSQL.model.ThanaPermission;
import com.PostGreSQL.model.User;
import com.PostGreSQL.repository.StudentSubmissionRepository;
import com.PostGreSQL.repository.ThanaPermissionRepository;
import com.PostGreSQL.repository.UserRepository;
import com.PostGreSQL.service.StudentService;
import com.PostGreSQL.springSecurity.JwtGenerator;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students/submit")
@Validated
public class StudentController {
    private final StudentService service;
    @Autowired
    private UserRepository userRepository;
    private final StudentSubmissionRepository repository;
    @Autowired
    private ThanaPermissionRepository thanaPermissionRepository;

    public StudentController(StudentSubmissionRepository repository,StudentService service) {
        this.repository = repository;
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<StudentSubmission>> getAllStudents() {
        List<StudentSubmission> students = repository.findByStatus("ACTIVE");
        return ResponseEntity.ok(students);
    }
    // POST /api/students  -> inserts one document
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<String> create(@Valid @RequestBody StudentSubmission body) throws BadRequestException {
        body.setId(null);
        // Example business rule: prevent specific dept
        if ("BBA".equalsIgnoreCase(body.getDivision())) {
            throw new BadRequestException2("Department is not allowed");
        }
        body.setStatus("ACTIVE");
        if(! body.getCollege().equals("")){
            body.setInformationType("HSC");
        }
        else{
            body.setInformationType("SSC");
        }

        var saved = repository.save(body); // DuplicateKeyException -> handled globally as 409
        return ResponseEntity.ok("Successfully Submitted");
    }
    @GetMapping("/page")
    public Page<StudentSubmission> getStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String type,   // <-- new param
            @RequestHeader("Authorization") String token
    ) {
        String finalToken = token.substring(7);
        String userMail = JwtGenerator.extractUsername(finalToken);

        User user = userRepository.findByEmail(userMail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(user.getUserType().equals("SuperAdmin") || user.getUserType().equals("Admin")){
            Pageable pageable = PageRequest.of( page, size, direction.equalsIgnoreCase("asc") ? org.springframework.data.domain.Sort.by(sortBy).ascending() : org.springframework.data.domain.Sort.by(sortBy).descending() );
            return service.getStudents("ACTIVE",type,pageable);
        }

        List<ThanaPermission> permissions = thanaPermissionRepository.findByUserId(user.getId());

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending()
        );

        // Collect results
        List<StudentSubmission> combined = new ArrayList<>();
        for (ThanaPermission tp : permissions) {
            Page<StudentSubmission> part = service.getStudentsByPermissionAndStatusAndInformationType(tp, "ACTIVE",type, pageable);
            combined.addAll(part.getContent());
        }

        return new PageImpl<>(combined, pageable, combined.size());
    }


    @GetMapping("/ssc/{roll}")
    public ResponseEntity<StudentSubmission> getBySscRoll(@PathVariable String roll) {
        return repository.findBySscRoll(roll)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public StudentSubmission getStudent(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    @PostMapping("/{id}")
    public StudentSubmission updateStudent(@PathVariable Long id,
                                           @RequestBody StudentSubmission updated) {
        StudentSubmission existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        existing.setBanglaName(updated.getBanglaName());
        existing.setEnglishName(updated.getEnglishName());
        existing.setSscRoll(updated.getSscRoll());
        existing.setHighSchool(updated.getHighSchool());
        existing.setSscDept(updated.getSscDept());
        existing.setSscResult(updated.getSscResult());
        existing.setSscMark(updated.getSscMark());
        existing.setCollege(updated.getCollege());
        existing.setHscDept(updated.getHscDept());
        existing.setHscRoll(updated.getHscRoll());
        existing.setHscResult(updated.getHscResult());
        existing.setHscMark(updated.getHscMark());
        existing.setDivision(updated.getDivision());
        existing.setDistrict(updated.getDistrict());
        existing.setUpazila(updated.getUpazila());
        existing.setTarget(updated.getTarget());
        existing.setMobile(updated.getMobile());
        existing.setGuardianMobile(updated.getGuardianMobile());
        existing.setEmail(updated.getEmail());
        existing.setComments(updated.getComments());
        existing.setStatus(updated.getStatus());

        return repository.save(existing);
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Student not found");
        }
        Optional<StudentSubmission> data= repository.findById(id);
        if(data.isPresent()){
            StudentSubmission st=data.get();
            st.setStatus("INACTIVE");
            repository.save(st);
        }
       // repository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}
