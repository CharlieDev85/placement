package com.blue.app.service;

import com.blue.app.model.RecommendationMade;
import com.blue.app.repository.RecommendationMadeRepository;
import org.springframework.stereotype.Service;

@Service
public class RecommendationMadeService {
    private final RecommendationMadeRepository recommendationMadeRepository;

    public RecommendationMadeService(RecommendationMadeRepository recommendationMadeRepository) {
        this.recommendationMadeRepository = recommendationMadeRepository;
    }

    public RecommendationMade save(RecommendationMade recommendationMade) {
        return recommendationMadeRepository.save(recommendationMade);
    }
}
