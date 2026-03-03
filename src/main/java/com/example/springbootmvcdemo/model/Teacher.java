package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private String employeeId;
    @Enumerated(EnumType.STRING)
    private Department department;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnoreProperties("teacher")
    List<SubjectEnrollment> enrollments;

    public enum Role { TEACHER, HOD, ADMIN}

    public enum Gender { MALE, FEMALE };

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<SubjectEnrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<SubjectEnrollment> enrollments) {
        this.enrollments = enrollments;
    }
}
