package com.example.springbootmvcdemo.service;

import com.example.springbootmvcdemo.repository.StudentRepository;
import com.example.springbootmvcdemo.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    GradingService gradingService;

    StudentRepository studentRepository;
    InstructorRepository instructorRepository;
    DashboardService(StudentRepository studentRepository, InstructorRepository instructorRepository){
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }
}
