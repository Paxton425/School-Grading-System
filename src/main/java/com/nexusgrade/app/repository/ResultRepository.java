package com.nexusgrade.app.repository;

import com.nexusgrade.app.model.Assessment;
import com.nexusgrade.app.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    List<Result> findByAssessment(Assessment assessment);
}
