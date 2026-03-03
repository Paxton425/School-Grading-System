package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    private Double score;
    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;
    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private SubjectEnrollment enrollment; // Links to the student-subject pair

    public Long getId() {
        return Id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public SubjectEnrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(SubjectEnrollment enrollment) {
        this.enrollment = enrollment;
    }
}