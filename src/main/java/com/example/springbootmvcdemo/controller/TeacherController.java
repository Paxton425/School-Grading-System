package com.example.springbootmvcdemo.controller;

import com.example.springbootmvcdemo.model.Teacher;
import com.example.springbootmvcdemo.repository.TeacherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
}
