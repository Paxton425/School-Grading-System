package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.Subject;
import com.example.springbootmvcdemo.model.SubjectEnrollment;
import com.example.springbootmvcdemo.model.Result;
import com.example.springbootmvcdemo.model.SubjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<SubjectEnrollment, Long> {
    List<SubjectEnrollment> findBySubject(Subject subject);

    Long countBySubject(Subject subject);
}
