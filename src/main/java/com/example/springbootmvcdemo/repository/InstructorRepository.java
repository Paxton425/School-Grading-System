package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, String> {
}
