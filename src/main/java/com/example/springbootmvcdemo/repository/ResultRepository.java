package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.Assessment;
import com.example.springbootmvcdemo.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    List<Result> findByAssessment(Assessment assessment);
}
