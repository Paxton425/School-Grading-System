package com.example.springbootmvcdemo.dto;

import com.example.springbootmvcdemo.model.Department;
import com.example.springbootmvcdemo.model.Phase;
import com.example.springbootmvcdemo.model.Subject;

public class SubjectDTO {
    public Long Id;
    public String name;
    public Department department;
    public Integer grade;
    public Phase phase;

    public SubjectDTO(Subject subject){
        this.Id = subject.getId();
        this.name = subject.getName();
        this.department = subject.getDepartment();
        this.grade = subject.getGrade();
        this.phase = subject.getPhaseByGrade(grade);
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
