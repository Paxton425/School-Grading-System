package com.nexusgrade.app.controller;

import com.nexusgrade.app.model.Result.Term;
import com.nexusgrade.app.repository.StudentRepository;
import com.nexusgrade.app.repository.InstructorRepository;
import com.nexusgrade.app.service.DashboardService;
import com.nexusgrade.app.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping(path = "/dashboard")
public class DashboardController {

    @Autowired GradingService gradingService;
    @Autowired DashboardService dashboardService;

    StudentRepository studentRepository;
    InstructorRepository instructorRepository;
    DashboardController(StudentRepository studentRepository, InstructorRepository instructorRepository){
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    @GetMapping
    public String getDashboardView(){
        return "dashboard/dashboard";
    }

    @GetMapping("/data")
    public ResponseEntity<?> dashboard() {

        Term currentTerm = Term.TERM_1;

        Map<String, Object> dashboardData = new HashMap<>();
        try{
            // Basic Stats
            Map<String, Object> stats = dashboardService.getStats(currentTerm);
            dashboardData.put("stats", stats);

            // Grade distribution
            Map<String, Double> gradeDistribution = dashboardService.getGradeDistribution(currentTerm);
            dashboardData.put("gradeDistribution", gradeDistribution);

            // Average scores per subject
            Map<String, Integer> subjectScores = new LinkedHashMap<>();
            subjectScores.put("Maths", 72);
            subjectScores.put("Physics", 68);
            subjectScores.put("Chemistry", 74);
            subjectScores.put("History", 85);
            subjectScores.put("English", 70);
            dashboardData.put("subjectPerformance", subjectScores);

            // Recent submissions
            List<Map<String, String>> submissions = new ArrayList<>();

            submissions.add(createSubmission("John Doe", "Math Assignment 1", "85"));
            submissions.add(createSubmission("Sarah Smith", "Physics Lab", "78"));
            submissions.add(createSubmission("Mike Brown", "Chemistry Test", "66"));
            submissions.add(createSubmission("Anna White", "Programming Task", "92"));

            dashboardData.put("submissions", submissions);

            return ResponseEntity.ok(dashboardData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went wrong");
        }
    }

    private Map<String, String> createSubmission(String student, String assignment, String grade) {
        Map<String, String> map = new HashMap<>();
        map.put("student", student);
        map.put("assignment", assignment);
        map.put("grade", grade);
        return map;
    }

}
