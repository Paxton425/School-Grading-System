package com.example.springbootmvcdemo.service;

import com.example.springbootmvcdemo.model.Result;
import com.example.springbootmvcdemo.model.Result.Term;
import com.example.springbootmvcdemo.model.Student;
import com.example.springbootmvcdemo.model.Subject;
import com.example.springbootmvcdemo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    @Autowired
    GradingService gradingService;

    StudentRepository studentRepository;
    InstructorRepository instructorRepository;
    AssessmentRepository assessmentRepository;
    ResultRepository resultRepository;

    DashboardService(StudentRepository studentRepository,
                     InstructorRepository instructorRepository,
                     AssessmentRepository assessmentRepository,
                     ResultRepository resultRepository) {
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.assessmentRepository = assessmentRepository;
        this.resultRepository = resultRepository;
    }

    public Map<String, Object> getStats(Term term) {
        List<Student> students = studentRepository.findAll();
        long totalStudents = students.size();
        long submissions = (resultRepository.count()); //%
        double averageGrade = gradingService.calculateResultsAverage(resultRepository.findAll());
        long passed = students.stream().filter(s -> gradingService.hasPassedTerm(s, term)).count();
        double passRate = ((passed / totalStudents) * 100);

        return Map.of(
                "studentsCount", totalStudents,
                "submissions", submissions,
                "averageGrade", averageGrade,
                "passRate", passRate
        );
    }

    public Map<String, Double> getGradeDistribution(Term term) {
        List<Result> results = resultRepository.findAll();
        int resultsCount = results.size();
        int excellentCount = 0;
        int goodCouunt = 0;
        int averageCount = 0;
        int badCount = 0;
        int poorCount = 0;

        for(Result result : results){
            double grade = gradingService.computeMark(result.getScore(), result.getAssessment().getMaxPoints());
            if(grade <= 30.00) //poor 30-0
                poorCount++;
            else if (grade < 40.00) //bad 39-30
                badCount++;
            else if (grade < 60.00) //Average 59-40
                averageCount++;
            else    //Excellent 100-60
                excellentCount++;
        }

        return Map.of("excellent", getPercent(excellentCount, resultsCount),
                "good", getPercent(goodCouunt, resultsCount),
                "average", getPercent(averageCount, resultsCount),
                "bad", getPercent(badCount, resultsCount),
                "poor", getPercent(badCount, resultsCount)
        );
    }

    double getPercent(int amount, int total){
        //DecimalFormat form
        return ((double)amount/(double)total)*100;
    }

}
