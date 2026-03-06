package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.model.Department;
import com.example.springbootmvcdemo.model.Teacher;
import com.example.springbootmvcdemo.repository.TeacherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(path = "/teachers")
public class TeacherController {

    TeacherRepository teacherRepository;
    TeacherController(TeacherRepository teacherRepository){
        this.teacherRepository = teacherRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public  String getAllTeachers(Model model){
        List<Teacher> teachers = teacherRepository.findAll();
        model.addAttribute("teachers", teachers);
        return "teachers/teachers-list";
    }

    @GetMapping("/create")
    public String crateTeacher(Model model) {
        // Pass a blank object for Thymeleaf binding
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("roles", Teacher.Role.values());
        model.addAttribute("genders", Teacher.Gender.values());
        model.addAttribute("departments", Department.values());

        return "teachers/add-teacher";
    }

}
