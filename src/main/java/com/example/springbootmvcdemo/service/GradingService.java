package com.example.springbootmvcdemo.service;

import com.example.springbootmvcdemo.model.Assessment;
import com.example.springbootmvcdemo.model.SubjectEnrollment;
import org.springframework.stereotype.Service;

@Service
public class GradingService {
    public double calculateFinalMark(SubjectEnrollment enrollment) {
        double sba = enrollment.getGrades().stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.SBA)
                .mapToDouble(g -> g.getScore())
                .average().orElse(0.0);

        double exam = enrollment.getGrades().stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.EXAM)
                .mapToDouble(g -> g.getScore())
                .findFirst().orElse(0.0);

        // Apply SA FET Phase Weighting: 25% SBA, 75% Exam
        return (sba * 0.25) + (exam * 0.75);
    }

    public int calculateLevel(double mark) {
        if (mark >= 80) return 7;
        if (mark >= 70) return 6;
        if(mark >= 60) return 5;
        if(mark >= 50) return 4;
        if(mark >= 40) return 3;
        if(mark >= 30) return 2;
        return 1;
    }
}
