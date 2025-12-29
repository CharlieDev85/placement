package com.blue.app.service;

import com.blue.app.model.Recommendation;
import com.blue.app.model.Result;
import com.blue.app.model.Schedule;
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

    public long countRecommendations() {
        return recommendationRepository.count();
    }

    public void seed() {
        Recommendation a1TemplateRecommendation7to8 = new Recommendation(Result.A1, Schedule.From7PMto8PM);
        Recommendation a2TemplateRecommendation7to8 = new Recommendation(Result.A2,Schedule.From7PMto8PM);
        Recommendation b1TemplateRecommendation7to8 = new Recommendation(Result.B1,Schedule.From7PMto8PM);
        Recommendation b2TemplateRecommendation7to8 = new Recommendation(Result.B2,Schedule.From7PMto8PM);
        Recommendation a1TemplateRecommendation8to9 = new Recommendation(Result.A1,Schedule.From8PMto9PM);
        Recommendation a2TemplateRecommendation8to9 = new Recommendation(Result.A2,Schedule.From8PMto9PM);
        Recommendation b1TemplateRecommendation8to9 = new Recommendation(Result.B1,Schedule.From8PMto9PM);
        Recommendation b2TemplateRecommendation8to9 = new Recommendation(Result.B2,Schedule.From8PMto9PM);

        recommendationRepository.save(a1TemplateRecommendation7to8);
        recommendationRepository.save(a2TemplateRecommendation7to8);
        recommendationRepository.save(b1TemplateRecommendation7to8);
        recommendationRepository.save(b2TemplateRecommendation7to8);
        recommendationRepository.save(a1TemplateRecommendation8to9);
        recommendationRepository.save(a2TemplateRecommendation8to9);
        recommendationRepository.save(b1TemplateRecommendation8to9);
        recommendationRepository.save(b2TemplateRecommendation8to9);
    }
}
