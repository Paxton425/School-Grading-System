package com.example.springbootmvcdemo.dto;

import com.example.springbootmvcdemo.model.Instructor;
import com.example.springbootmvcdemo.model.SchoolClass;
import com.example.springbootmvcdemo.model.Student;
import com.example.springbootmvcdemo.model.SubjectsStream;

import java.util.List;

public class SchoolClassDTO {
    private Long id;
    private String title;
    private Integer grade;
    private Integer classYear;
    private SubjectsStream subjectsStream;
    private List<Instructor> instructors;
    private List<Student> students;

    public SchoolClassDTO(Long id, String title, Integer grade, Integer classYear, SubjectsStream subjectsStream, List<Instructor> instructors, List<Student> students) {
        this.id = id;
        this.title = title;
        this.grade = grade;
        this.classYear = classYear;
        this.subjectsStream = subjectsStream;
        this.instructors = instructors;
        this.students = students;
    }

    public SchoolClassDTO(SchoolClass schoolClass) {
        this.id = schoolClass.getId();
        this.title = schoolClass.getTitle();
        this.grade = schoolClass.getGrade();
        this.classYear = schoolClass.getClassYear();
        this.subjectsStream = schoolClass.getSubjectsStream();
        //this.instructors = schoolClass.getInstructors();
        //this.instructors = schoolClass.getInstructors();
        //this.students = schoolClass.getStudents();
    }

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
