package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.model.SubjectGrades;
import com.example.springbootmvcdemo.repository.GradesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private GradesRepository gradesRepository;

    SubjectController(GradesRepository gradesRepository){
        this.gradesRepository = gradesRepository;
    }


    @RequestMapping(path="/")
    public String Index(){
        return "index";
    }

    @GetMapping("/subject/grades/{id}")
    public String viewSubjectEnrollmentGrades(@PathVariable Long id, Model model) {
        // Find the specific record that links this student to this subject
        SubjectGrades grades = gradesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject grades record not found for this subject."));

        model.addAttribute("grades", grades);
        model.addAttribute("student", grades.getStudent());
        model.addAttribute("subject", grades.getSubject());
        model.addAttribute("results", grades.getGrades()); // The list of Result entities

        return "subjects/subject-grades";
    }
}
