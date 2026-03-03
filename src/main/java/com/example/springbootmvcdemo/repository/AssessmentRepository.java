package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, String> {
}
