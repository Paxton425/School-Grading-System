package com.example.springbootmvcdemo.loader;

import com.example.springbootmvcdemo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectsLoader {

    @Autowired
    private StudentRepository studentRepo;
    @Autowired private SubjectRepository subjectRepo;
    @Autowired private AssessmentRepository assessmentRepo;
    @Autowired private ResultRepository achievementRepo;
    @Autowired private InstructorRepository teacherRepo;
    @Autowired private ClassRepository classRepository;

    public void loadSubjectStreams(){
    }
}
