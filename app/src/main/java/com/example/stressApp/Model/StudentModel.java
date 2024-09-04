package com.example.stressApp.Model;

public class StudentModel {
    private String name;
    private String idNumber;
    private String email;
    private String phoneNumber;
    private String role;

    public StudentModel() {}

    public StudentModel(String name, String idNumber, String email, String phoneNumber, String role) {
        this.name = name;
        this.idNumber = idNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
