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
        int marksCount = 0;
        TreeMap<String, TreeMap<Term, TermResult>> results = new TreeMap<>();
        HashMap<Term, Double> totalsPerTerm = new HashMap<>();
        HashMap<Term, Double> averagePerTerm = new HashMap<>();
        int subjectCount = student.getEnrollments().size();

        for (SubjectEnrollment enrollment : student.getEnrollments()) {
            TreeMap<Term, TermResult> termResults = new TreeMap<>();
            for (Term term : Term.values()) {
                double finalMark = gradingService.calculateFinalMark(enrollment, term);
                double averageMark = gradingService.calculateGradeAverage(enrollment, term);
                Integer level = gradingService.calculateLevel(finalMark);

                termResults.put(term, new TermResult(finalMark, averageMark, level));

                // Just do the running total here
                totalsPerTerm.put(term, totalsPerTerm.getOrDefault(term, 0.0) + finalMark);

                // Global stats
                highestMark = Math.max(highestMark, finalMark);
                lowestMark = Math.min(lowestMark, finalMark);
                overallTotal += finalMark;
                marksCount++;
            }
            results.put(enrollment.getSubject().getName(), termResults);
        }

        // NOW calculate averages after all totals are complete
        for (Term term : Term.values()) {
            double termTotal = totalsPerTerm.getOrDefault(term, 0.0);
            averagePerTerm.put(term, subjectCount > 0 ? termTotal / subjectCount : 0.0);
        }

        overallAverage = marksCount > 0 ? overallTotal / marksCount : 0.0;

        report.setResults(results);
        report.setTotalsPerTerm(totalsPerTerm);
        report.setAveragePerTerm(averagePerTerm);
        report.setOverallTotal(overallTotal);
        report.setOverallAverage(overallAverage);
        report.setHighestMark(highestMark);
        report.setLowestMark(lowestMark);

        return report;
    }

}