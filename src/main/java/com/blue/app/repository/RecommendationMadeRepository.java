package com.blue.app.repository;

import com.blue.app.model.RecommendationMade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationMadeRepository extends JpaRepository<RecommendationMade, Long> {
}
