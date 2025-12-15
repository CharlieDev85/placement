package com.blue.app.service;

import com.blue.app.model.Recommendation;
import com.blue.app.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }
    public void save(Recommendation recommendation){
        recommendationRepository.save(recommendation);
    }
    public List<Recommendation> findAll(){
        return recommendationRepository.findAll();
    }

}
