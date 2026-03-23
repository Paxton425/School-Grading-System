package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.model.Result;
import com.example.springbootmvcdemo.model.Student;
import com.example.springbootmvcdemo.repository.StudentRepository;
import com.example.springbootmvcdemo.repository.TeacherRepository;
import com.example.springbootmvcdemo.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/")
public class DashboardController {

    @Autowired
    GradingService gradingService;

    StudentRepository studentRepository;
    TeacherRepository teacherRepository;
    DashboardController(StudentRepository studentRepository, TeacherRepository teacherRepository){
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // Stats
        model.addAttribute("totalStudents", 320);
        model.addAttribute("assignmentsSubmitted", 890);
        model.addAttribute("averageGrade", 76);
        model.addAttribute("passRate", 82);

        // Grade distribution
        Map<String, Integer> gradeDistribution = new LinkedHashMap<>();
        gradeDistribution.put("A", 80);
        gradeDistribution.put("B", 120);
        gradeDistribution.put("C", 70);
        gradeDistribution.put("D", 30);
        gradeDistribution.put("F", 20);

        model.addAttribute("gradeDistribution", gradeDistribution);

        // Average scores per subject
        Map<String, Integer> subjectScores = new LinkedHashMap<>();
        subjectScores.put("Math", 72);
        subjectScores.put("Physics", 68);
        subjectScores.put("Chemistry", 74);
        subjectScores.put("Computer Science", 85);
        subjectScores.put("Biology", 70);

        model.addAttribute("subjectScores", subjectScores);

        // Recent submissions
        List<Map<String, String>> submissions = new ArrayList<>();

        submissions.add(createSubmission("John Doe", "Math Assignment 1", "85"));
        submissions.add(createSubmission("Sarah Smith", "Physics Lab", "78"));
        submissions.add(createSubmission("Mike Brown", "Chemistry Test", "66"));
        submissions.add(createSubmission("Anna White", "Programming Task", "92"));

        model.addAttribute("submissions", submissions);

        return "dashboard/dashboard";
    }

    private Map<String, String> createSubmission(String student, String assignment, String grade) {
        Map<String, String> map = new HashMap<>();
        map.put("student", student);
        map.put("assignment", assignment);
        map.put("grade", grade);
        return map;
    }

}
