package com.example.springbootmvcdemo.service;

import com.example.springbootmvcdemo.model.Assessment;
import com.example.springbootmvcdemo.model.Result.Term;
import com.example.springbootmvcdemo.model.SubjectEnrollment;
import org.springframework.stereotype.Service;

@Service
public class GradingService {
    public double calculateMaxGrade(SubjectEnrollment en){
        double max = en.getGrades().stream()
                .filter(a -> a.getAssessment().getType() == Assessment.AssessmentType.SBA)
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .max().orElse(0.0);
        return max;
    }
    public double calculateMinGrade(SubjectEnrollment en){
        double min = en.getGrades().stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.SBA)
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .min().orElse(0.0);
        return min;
    }
    public double calculateGradeAverage(SubjectEnrollment en, Term term){
        double average = en.getGrades().stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.SBA && g.getTerm() == term)
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .average().orElse(0.0);
        return  average;
    }
    public double calculateFinalMark(SubjectEnrollment en, Term term) {
        double sba = en.getGrades().stream()
                .filter(g -> g.getAssessment().getType().equals(Assessment.AssessmentType.SBA) && g.getTerm().equals(term))
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
                .average().orElse(0.0);

        double exam = en.getGrades().stream()
                .filter(g -> g.getAssessment().getType() == Assessment.AssessmentType.EXAM && g.getTerm() == term)
                .mapToDouble(s -> computeMark(s.getScore(),s.getAssessment().getMaxPoints()))
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

    double computeMark(Integer score, Integer maxPoints){
        if (maxPoints == 0) return 0.0; // Avoid division by zero
        return (score / (double) maxPoints) * 100; //Mark%
    }
}
