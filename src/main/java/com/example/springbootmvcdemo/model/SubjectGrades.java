package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class SubjectGrades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties("subjectGrades")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    @JsonIgnoreProperties("subjectGrades")
    private Subject subject;
    @ManyToOne
    @JsonIgnoreProperties("subjectGrades")
    private Instructor instructor;
    @OneToMany(mappedBy = "subjectGrades", cascade = CascadeType.ALL)  // "subjectGrade" not "subject_grades"
    @JsonIgnoreProperties("subjectGrades")
    private List<Result> grades;

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Result> getGrades() {
        return grades;
    }

    public void setGrades(List<Result> grades) {
        this.grades = grades;
    }
}