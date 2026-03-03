package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findSubjectBySubjectCode(String code);
}
