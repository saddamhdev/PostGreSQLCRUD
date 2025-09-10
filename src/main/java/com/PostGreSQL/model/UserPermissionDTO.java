package com.PostGreSQL.model;
import java.util.List;

public class UserPermissionDTO {
    private Long userId;
    private String userName;
    private String division;
    private String district;
    private List<String> thanaNames;

    // constructor
    public UserPermissionDTO(Long userId, String userName, String division, String district, List<String> thanaNames) {
        this.userId = userId;
        this.userName = userName;
        this.division = division;
        this.district = district;
        this.thanaNames = thanaNames;
    }

    // getters/setters
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getDivision() { return division; }
    public String getDistrict() { return district; }
    public List<String> getThanaNames() { return thanaNames; }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setThanaNames(List<String> thanaNames) {
        this.thanaNames = thanaNames;
    }
}
