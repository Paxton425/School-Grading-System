package com.example.springbootmvcdemo.service;

import com.example.springbootmvcdemo.repository.StudentRepository;
import com.example.springbootmvcdemo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    GradingService gradingService;

    StudentRepository studentRepository;
    TeacherRepository teacherRepository;
    DashboardService(StudentRepository studentRepository, TeacherRepository teacherRepository){
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }
}
