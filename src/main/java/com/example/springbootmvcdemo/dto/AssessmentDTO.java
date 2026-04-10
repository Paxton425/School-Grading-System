package com.example.springbootmvcdemo.dto;

import com.example.springbootmvcdemo.model.Assessment;
import com.example.springbootmvcdemo.model.Assessment.AssessmentType;
import com.example.springbootmvcdemo.model.SchoolClass;
import com.example.springbootmvcdemo.model.Subject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

public class AssessmentDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate assignmentIssueDate;
    private LocalDate assignmentDeadline;
    private Integer maxPoints;
    private AssessmentType type; // SBA, PAT, EXAM
    private SubjectDTO subject;
    private List<SchoolClass> schoolClasses;

    public AssessmentDTO(Assessment assessment){
        this.id = assessment.getId();
        this.title = assessment.getTitle();
        this.description = assessment.getDescription();
        this.assignmentIssueDate = assessment.getAssignmentIssueDate();
        this.assignmentDeadline = assessment.getAssignmentDeadline();
        this.maxPoints = assessment.getMaxPoints();
        this.type = assessment.getType();
        this.subject = new SubjectDTO(assessment.getSubject());
    }
}
