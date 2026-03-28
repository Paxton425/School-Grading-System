package com.example.springbootmvcdemo.dto;
import com.example.springbootmvcdemo.model.SchoolClass;
import com.example.springbootmvcdemo.model.Student.Gender;
import com.example.springbootmvcdemo.model.SubjectGrades;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class StudentDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthDay;
    private List<SubjectDTO> subjects;
    private List<SubjectGrades> subjectGrades;
    private SchoolClassDTO schoolClassDTO;

    public StudentDTO(UUID id, String firstName, String lastName, Gender gender, LocalDate birthDay, SchoolClass schoolClass) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDay = birthDay;
        this.schoolClassDTO = new SchoolClassDTO(schoolClass);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public SchoolClassDTO getSchoolClassDTO() {
        return schoolClassDTO;
    }

    public void setSchoolClassDTO(SchoolClassDTO schoolClassDTO) {
        this.schoolClassDTO = schoolClassDTO;
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

    public List<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDTO> enrollmentSubjects) {
        this.subjects = enrollmentSubjects;
    }
}
