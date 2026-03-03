package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
}
