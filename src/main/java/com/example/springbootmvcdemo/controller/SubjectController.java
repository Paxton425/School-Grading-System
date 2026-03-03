package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.model.SubjectEnrollment;
import com.example.springbootmvcdemo.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private EnrollmentRepository enrollmentRepository;

    SubjectController(EnrollmentRepository enrollmentRepository){
        this.enrollmentRepository = enrollmentRepository;
    }


    @RequestMapping(path="/")
    public String Index(){
        return "index";
    }

    @GetMapping("/subject/{enrollmentId}/grades")
    public String viewSubjectEnrollmentGrades(@PathVariable Long enrollmentId, Model model) {
        // Find the specific enrollment that links this student to this subject
        SubjectEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment record not found for this subject."));

        model.addAttribute("enrollment", enrollment);
        model.addAttribute("student", enrollment.getStudent());
        model.addAttribute("subject", enrollment.getSubject());
        model.addAttribute("results", enrollment.getGrades()); // The list of Result entities

        return "subjects/subject-grades";
    }
}
