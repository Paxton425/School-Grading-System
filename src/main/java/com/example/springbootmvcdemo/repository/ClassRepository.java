package com.example.springbootmvcdemo.repository;

import com.example.springbootmvcdemo.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<SchoolClass, Long> {
    SchoolClass findSchoolClassByGradeContainsAndTitleOrderByTitle(Integer grade, String title); //e.g Grade=7,Title=A
}
