package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.repository.ResultRepository;
import com.example.springbootmvcdemo.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private SubjectRepository subjectRepository;
    private ResultRepository resultRepository;

    SubjectController(ResultRepository resultRepository, SubjectRepository subjectRepository){
        this.resultRepository = resultRepository;
        this.subjectRepository = subjectRepository;
    }


    @RequestMapping(path="/")
    public String Index(){
        return "index";
    }

    @GetMapping("/subject/grades/{id}")
    public String viewSubjectEnrollmentGrades(@PathVariable Long id, Model model) {
        // Find the specific record that links this student to this subject

        return "subjects/subject-grades";
    }
}
