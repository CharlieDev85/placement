package com.blue.app.service;

import com.blue.app.model.RecommendationMade;
import com.blue.app.model.Student;
import com.blue.app.repository.RecommendationMadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationMadeService {
    private final RecommendationMadeRepository recommendationMadeRepository;

    public RecommendationMadeService(RecommendationMadeRepository recommendationMadeRepository) {
        this.recommendationMadeRepository = recommendationMadeRepository;
    }

    public RecommendationMade save(RecommendationMade recommendationMade) {
        return recommendationMadeRepository.save(recommendationMade);
    }

    public List<RecommendationMade> findAll() {
        return recommendationMadeRepository.findAll();
    }

    public Optional<RecommendationMade> findLatestForStudent(Student student) {
        return recommendationMadeRepository.findTopByAttemptStudentOrderByCreatedAtDesc(student);
    }
}
