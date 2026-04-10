package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.SchoolClass;
import com.example.springbootmvcdemo.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String searchValue1, String searchValue2, Pageable pageable);

    long countBySchoolClass(SchoolClass schoolClass);
}
