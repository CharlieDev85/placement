package com.blue.app.repository;

import com.blue.app.model.Recommendation;
import com.blue.app.model.Result;
import com.blue.app.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Optional<Recommendation> findFirstByResultAndScheduleChosen(Result result, Schedule scheduleChosen);
}
