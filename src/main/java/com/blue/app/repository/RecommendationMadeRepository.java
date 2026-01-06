package com.blue.app.repository;

import com.blue.app.model.RecommendationMade;
import com.blue.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationMadeRepository extends JpaRepository<RecommendationMade, Long> {
    Optional<RecommendationMade> findTopByAttemptStudentOrderByCreatedAtDesc(Student student);
}
