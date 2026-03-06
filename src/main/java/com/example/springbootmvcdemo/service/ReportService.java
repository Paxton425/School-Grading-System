package com.example.springbootmvcdemo.service;

import com.example.springbootmvcdemo.dto.*;
import com.example.springbootmvcdemo.dto.StudentReportDTO.TermResult;
import com.example.springbootmvcdemo.model.Result.*;
import com.example.springbootmvcdemo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

@Service
public class ReportService {
    @Autowired
    private GradingService gradingService;

    public StudentReportDTO generateReport(Student student) {
        StudentReportDTO report = new StudentReportDTO();
        report.setStudentId(student.getId());
        report.setFirstName(student.getFirstName());
        report.setLastName(student.getLastName());
        double highestMark = 0.0;
        double lowestMark = 100.0;
        double overallTotal = 0.0;
        double overallAverage = 0.0;
        double marksCount = 0;
        TreeMap<String, TreeMap<Term, TermResult>> results = new TreeMap<>();
        HashMap<Term, Double> totalsPerTerm = new HashMap<>();
        HashMap<Term, Double> averagePerTerm = new HashMap<>();

        for(SubjectEnrollment enrollment : student.getEnrollments()){
            TreeMap<Term, TermResult> termResults = new TreeMap<>();
            for(Term term : Term.values()){ //Setting subject results for each term
                double finalMark = gradingService.calculateFinalMark(enrollment, term);
                double averageMark = gradingService.calculateGradeAverage(enrollment, term);
                Integer level = gradingService.calculateLevel(finalMark);
                TermResult termResult = new TermResult(
                        finalMark,
                        averageMark,
                        level
                );
                termResults.put(term, termResult);
                totalsPerTerm.put(term, totalsPerTerm.getOrDefault(term, 0.0)+finalMark);
                averagePerTerm.put(term, totalsPerTerm.getOrDefault(term, 0.0)/student.getEnrollments().size());
                highestMark = (finalMark > highestMark)? finalMark : highestMark;
                lowestMark = (finalMark < lowestMark)? finalMark : lowestMark;
                overallTotal = overallTotal+finalMark;
                marksCount++;
            }
            results.put(enrollment.getSubject().getName(), termResults);
        }
        overallAverage = overallTotal/marksCount;

        report.setResults(results);
        report.setOverallTotal(overallTotal);
        report.setOverallAverage(overallAverage);
        report.setLowestMark(lowestMark);
        report.setOverallAverage(highestMark);

        return report;
    }
}