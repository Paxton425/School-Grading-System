package com.example.springbootmvcdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer maxPoints;
    @Enumerated(EnumType.STRING)
    private Term term;
    @Enumerated(EnumType.STRING)
    private AssessmentType type; // SBA, PAT, EXAM
    @ManyToOne
    @JsonIgnoreProperties("assessments")
    private Subject subject;

    public enum AssessmentType { SBA, PAT, EXAM }
    public enum Term {
        TERM_1, TERM_2, TERM_3, TERM_4
    };

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
