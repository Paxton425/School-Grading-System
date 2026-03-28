package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.Subject;
import com.example.springbootmvcdemo.model.SubjectGrades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradesRepository extends JpaRepository<SubjectGrades, Long> {
    List<SubjectGrades> findBySubject(Subject subject);

    Long countBySubject(Subject subject);
}
