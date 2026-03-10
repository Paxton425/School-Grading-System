package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    private Integer score; //eg. 90/100
    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;
    @Enumerated(EnumType.STRING)
    private Term term;
    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private SubjectEnrollment enrollment; // Links to the student-subject pair

    public enum Term {
        TERM_1, TERM_2, TERM_3, TERM_4
    };

    public Long getId() {
        return Id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
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

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}