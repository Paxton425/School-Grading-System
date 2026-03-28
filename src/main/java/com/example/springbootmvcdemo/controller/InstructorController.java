package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.model.Department;
import com.example.springbootmvcdemo.model.Instructor;
import com.example.springbootmvcdemo.repository.InstructorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/instructors")
public class InstructorController {

    InstructorRepository instructorRepository;
    InstructorController(InstructorRepository instructorRepository){
        this.instructorRepository = instructorRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public  String getAllTeachers(Model model){
        List<Instructor> instructors = instructorRepository.findAll();
        model.addAttribute("instructors", instructors);
        return "instructors/instructors-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("instructor", new Instructor());
        model.addAttribute("departments", Department.values());
        model.addAttribute("roles", Instructor.Role.values());
        model.addAttribute("genders", Instructor.Gender.values());
        return "instructors/instructor-form";
    }

    @PostMapping("/save")
    public String saveInstructor(@ModelAttribute Instructor instructor) {
        instructorRepository.save(instructor);
        return "redirect:/instructors";
    }

}
