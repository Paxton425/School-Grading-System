package com.example.springbootmvcdemo.dto;
import com.example.springbootmvcdemo.model.Student.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class StudentDTO {

    public StudentDTO(UUID id, String firstName, String lastName, Gender gender, Integer grade) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.grade = grade;
    }

    public UUID id;
    public String firstName;
    public String lastName;
    public Integer grade;
    public Gender gender;
    public LocalDate birthDay;
    public List<SubjectDTO> enrollmentSubjects;
}
