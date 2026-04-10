package com.nexusgrade.app.service;

import com.nexusgrade.app.dto.*;
import com.nexusgrade.app.dto.StudentReportDTO;
import com.nexusgrade.app.dto.StudentReportDTO.TermResult;
import com.nexusgrade.app.model.Result.*;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.Student;
import com.nexusgrade.app.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {
    @Autowired
    private GradingService gradingService;

    public StudentReportDTO generateReport(Student student) {
        StudentReportDTO report = new StudentReportDTO();
        report.setStudent(student);
        double highestMark = 0.0;
        double lowestMark = 100.0;
        double overallTotal = 0.0;
        double overallAverage = 0.0;
        int marksCount = 0;
        TreeMap<String, TreeMap<Term, TermResult>> results = new TreeMap<>();
        HashMap<Term, Double> totalsPerTerm = new HashMap<>();
        HashMap<Term, Double> averagePerTerm = new HashMap<>();
        int subjectCount = student.getSchoolClass().getSubjects().size();

        for (Subject subject : student.getSchoolClass().getSubjects()) {
            TreeMap<Term, TermResult> termResults = new TreeMap<>();

            for (Term term : Term.values()) {
                Map<String, Number> termResult = gradingService.calculateTermResult(student.getResults(), subject, term);

                termResults.put(
                        term,
                        new TermResult(
                                termResult.get("results").doubleValue(),
                                termResult.get("average").doubleValue(),
                                termResult.get("level").intValue())
                );

                // Just do the running total here
                totalsPerTerm.put(term, totalsPerTerm.getOrDefault(term, 0.0) + termResult.get("results").doubleValue());

                // Global stats
                highestMark = Math.max(highestMark, termResult.get("results").doubleValue());
                lowestMark = Math.min(lowestMark, termResult.get("results").doubleValue());
                overallTotal += termResult.get("results").doubleValue();
                marksCount++;
            }
            results.put(subject.getName(), termResults);
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