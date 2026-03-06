package com.example.springbootmvcdemo.dto;

import com.example.springbootmvcdemo.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

public class SubjectEnrollmentDTO {
    public SubjectEnrollmentDTO(Long id, StudentDTO student, SubjectDTO subject, Teacher teacher, List<ResultDTO> grades){}
    public Long id;
    public StudentDTO student;
    public SubjectDTO subject;
    public Teacher teacher;
    public List<ResultDTO> grades;

    public class ResultDTO{
        Long Id;
        private Double score;
        private Assessment assessment;
        private SubjectEnrollment enrollment;

    }
}