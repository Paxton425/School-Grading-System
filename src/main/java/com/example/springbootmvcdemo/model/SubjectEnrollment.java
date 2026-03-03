package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class SubjectEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties("enrollments")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    @JsonIgnoreProperties("enrollments")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties("enrollments")
    private Teacher teacher;
    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("enrollment")
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Result> getGrades() {
        return grades;
    }

    public void setGrades(List<Result> grades) {
        this.grades = grades;
    }
}