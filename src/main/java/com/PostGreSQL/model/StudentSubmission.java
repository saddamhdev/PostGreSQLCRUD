package com.PostGreSQL.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /* ================== Personal Info ================== */
    @NotBlank(message = "Full name (Bangla) is required")
    @Size(min = 3, max = 80)
    @Column(nullable = false, length = 80)
    private String banglaName;

    @NotBlank(message = "Full name (English) is required")
    @Size(min = 3, max = 80)
    @Column(nullable = false, length = 80)
    private String englishName;

    /* ================== Academic Info ================== */
    @NotBlank @Size(max = 20) @Column(nullable = false, length = 20)
    private String sscRoll;

    @NotBlank @Size(max = 120) @Column(nullable = false, length = 120)
    private String highSchool;

    @NotBlank @Size(max = 60) @Column(nullable = false, length = 60)
    private String sscDept;

    @NotBlank @Size(max = 20) @Column(nullable = false, length = 20)
    private String sscResult;

    @NotBlank @Size(max = 20) @Column(nullable = false, length = 20)
    private String sscMark;

   // @NotBlank @Size(max = 20) @Column(nullable = false, length = 20)
    private String hscRoll;

  //  @NotBlank @Size(max = 120) @Column(nullable = false, length = 120)
    private String college;

  //  @NotBlank @Size(max = 60) @Column(nullable = false, length = 60)
    private String hscDept;

   // @NotBlank @Size(max = 20) @Column(nullable = false, length = 20)
    private String hscResult;

   // @NotBlank @Size(max = 20) @Column(nullable = false, length = 20)
    private String hscMark;

    /* ================== Location ================== */
    @NotBlank @Size(max = 60) @Column(nullable = false, length = 60)
    private String division;

    @NotBlank @Size(max = 60) @Column(nullable = false, length = 60)
    private String district;

    @NotBlank @Size(max = 60) @Column(nullable = false, length = 60)
    private String upazila;

    /* ================== Career ================== */
    @NotBlank @Size(max = 120) @Column(nullable = false, length = 120)
    private String target;

    /* ================== Contact ================== */
    @NotBlank
    @Pattern(regexp = "^01[3-9]\\d{8}$")
    @Column(nullable = false, length = 15)
    private String mobile;

    @NotBlank
    @Pattern(regexp = "^01[3-9]\\d{8}$")
    @Column(nullable = false, length = 15)
    private String guardianMobile;

    @NotBlank
    @Email
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String email;

    /* ================== Other ================== */
    @Size(max = 1000) @Column(length = 1000)
    private String comments;

    @Column(nullable = false)
    private Boolean agree;

    private String status;

    private String informationType;

    /* ================== Timestamps ================== */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBanglaName() {
        return banglaName;
    }

    public void setBanglaName(String banglaName) {
        this.banglaName = banglaName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getSscRoll() {
        return sscRoll;
    }

    public void setSscRoll(String sscRoll) {
        this.sscRoll = sscRoll;
    }

    public String getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    public String getSscDept() {
        return sscDept;
    }

    public void setSscDept(String sscDept) {
        this.sscDept = sscDept;
    }

    public String getSscResult() {
        return sscResult;
    }

    public void setSscResult(String sscResult) {
        this.sscResult = sscResult;
    }

    public String getSscMark() {
        return sscMark;
    }

    public void setSscMark(String sscMark) {
        this.sscMark = sscMark;
    }

    public String getHscRoll() {
        return hscRoll;
    }

    public void setHscRoll(String hscRoll) {
        this.hscRoll = hscRoll;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getHscDept() {
        return hscDept;
    }

    public void setHscDept(String hscDept) {
        this.hscDept = hscDept;
    }

    public String getHscResult() {
        return hscResult;
    }

    public void setHscResult(String hscResult) {
        this.hscResult = hscResult;
    }

    public String getHscMark() {
        return hscMark;
    }

    public void setHscMark(String hscMark) {
        this.hscMark = hscMark;
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

    public String getUpazila() {
        return upazila;
    }

    public void setUpazila(String upazila) {
        this.upazila = upazila;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGuardianMobile() {
        return guardianMobile;
    }

    public void setGuardianMobile(String guardianMobile) {
        this.guardianMobile = guardianMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public String getInformationType() {
        return informationType;
    }

    public void setInformationType(String informationType) {
        this.informationType = informationType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
