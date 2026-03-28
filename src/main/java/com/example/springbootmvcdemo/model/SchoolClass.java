package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer grade;
    private Integer classYear;
    @ManyToOne
    private SubjectsStream subjectsStream;
    @ManyToMany(mappedBy = "schoolClasses")
    @JsonIgnoreProperties("schoolClasses")
    private List<Instructor> instructors;
    @OneToMany(mappedBy = "schoolClass")
    @JsonIgnoreProperties("schoolClass")
    private List<Student> students;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getClassYear() {
        return classYear;
    }

    public void setClassYear(Integer classYear) {
        this.classYear = classYear;
    }

    public SubjectsStream getSubjectsStream() {
        return subjectsStream;
    }

    public void setSubjectsStream(SubjectsStream subjectsStream) {
        this.subjectsStream = subjectsStream;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
