package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String searchValue1, String searchValue2, Pageable pageable);

    long countBySchoolClass(SchoolClass schoolClass);
}
