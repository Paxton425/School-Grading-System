package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String subjectCode; // e.g., MATH10, PHYS12
    @Column(nullable = false)
    private String name; // e.g., Mathematics
    private Department department;
    private Integer grade;
    private Double sbaWeight;  // e.g., 0.25 (25%)
    private Double examWeight; // e.g., 0.75 (75%)

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("subject")
    private List<Assessment> assessments;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("subject")
    private List<SubjectGrades> enrollments;
    @ManyToOne
    private SubjectsStream subjectsStream;

    public Long getId() {
        return id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Phase getPhaseByGrade(Integer grade){
        return Phase.getByGrade(grade);
    }

    public Double getSbaWeight() {
        return sbaWeight;
    }

    public void setSbaWeight(Double sbaWeight) {
        this.sbaWeight = sbaWeight;
    }

    public Double getExamWeight() {
        return examWeight;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public void setExamWeight(Double examWeight) {
        this.examWeight = examWeight;
    }

    public List<SubjectGrades> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<SubjectGrades> enrollments) {
        this.enrollments = enrollments;
    }

    public SubjectsStream getSubjectsStream() {
        return subjectsStream;
    }

    public void setSubjectsStream(SubjectsStream subjectsStream) {
        this.subjectsStream = subjectsStream;
    }
}

