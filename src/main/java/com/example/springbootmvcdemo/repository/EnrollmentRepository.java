package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.SubjectEnrollment;
import com.example.springbootmvcdemo.model.Result;
import com.example.springbootmvcdemo.model.SubjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<SubjectEnrollment, Long> {
}
