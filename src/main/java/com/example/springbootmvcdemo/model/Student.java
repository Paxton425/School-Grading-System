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
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthDay;
    @ManyToOne
    @JsonIgnoreProperties("student")
    private SchoolClass schoolClass;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("student")
    private List<SubjectGrades> subjectGrades;

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

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public List<SubjectGrades> getSubjectGrades() {
        return subjectGrades;
    }

    public void setSubjectGrades(List<SubjectGrades> subjectGrades) {
        this.subjectGrades = subjectGrades;
    }
}
