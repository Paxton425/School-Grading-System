package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private Integer grade;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthDay;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("student")
    private List<SubjectEnrollment> enrollments;

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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public List<SubjectEnrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<SubjectEnrollment> enrollments) {
        this.enrollments = enrollments;
    }
}
