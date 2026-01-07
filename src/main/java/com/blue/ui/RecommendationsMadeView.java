package com.blue.ui;

import com.blue.app.model.Attempt;
import com.blue.app.model.Recommendation;
import com.blue.app.model.RecommendationMade;
import com.blue.app.service.RecommendationMadeService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Route("recommendations-made")
public class RecommendationsMadeView extends VerticalLayout {

    private final RecommendationMadeService recommendationMadeService;
    private final Grid<RecommendationMade> recommendationGrid = new Grid<>(RecommendationMade.class, false);

    @Autowired
    public RecommendationsMadeView(RecommendationMadeService recommendationMadeService) {
        this.recommendationMadeService = recommendationMadeService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        configureGrid();
        loadRecommendations();
        add(recommendationGrid);
    }

    private void configureGrid() {
        recommendationGrid.addColumn(recommendation -> {
            Attempt attempt = recommendation.getAttempt();
            if (attempt == null || attempt.getStudent() == null) {
                return "-";
            }
            return attempt.getStudent().getName();
        }).setHeader("Student").setAutoWidth(true);

        recommendationGrid.addColumn(recommendation -> {
            Attempt attempt = recommendation.getAttempt();
            if (attempt == null || attempt.getQuiz() == null) {
                return "-";
            }
            return attempt.getQuiz().getId();
        }).setHeader("Quiz ID").setAutoWidth(true);

        recommendationGrid.addColumn(recommendation -> {
            Recommendation rec = recommendation.getRecommendation();
            if (rec == null || rec.getResult() == null) {
                return "-";
            }
            return rec.getResult().getDisplayName();
        }).setHeader("Result").setAutoWidth(true);

        recommendationGrid.addColumn(recommendation -> {
            Recommendation rec = recommendation.getRecommendation();
            if (rec == null || rec.getScheduleChosen() == null) {
                return "-";
            }
            return rec.getScheduleChosen().getDisplayName();
        }).setHeader("Schedule chosen").setAutoWidth(true);

        recommendationGrid.addColumn(recommendation -> {
            Recommendation rec = recommendation.getRecommendation();
            if (rec == null || rec.getRecommendedCourse() == null) {
                return "-";
            }
            return rec.getRecommendedCourse().getDisplayName();
        }).setHeader("Course").setAutoWidth(true);

        recommendationGrid.addColumn(recommendation -> {
            Recommendation rec = recommendation.getRecommendation();
            if (rec == null || rec.getRecommendedSchedule() == null) {
                return "-";
            }
            return rec.getRecommendedSchedule().getDisplayName();
        }).setHeader("Recommended schedule").setAutoWidth(true);

        recommendationGrid.addColumn(recommendation -> {
            Recommendation rec = recommendation.getRecommendation();
            if (rec == null || rec.getPercentage() == null) {
                return "-";
            }
            return NumberFormat.getPercentInstance(Locale.US).format(rec.getPercentage());
        }).setHeader("Percentage").setAutoWidth(true);

        recommendationGrid.addColumn(RecommendationMade::getCreatedAt)
                .setHeader("Created At")
                .setAutoWidth(true);
    }

    private void loadRecommendations() {
        List<RecommendationMade> recommendations = recommendationMadeService.findAll();
        recommendationGrid.setItems(recommendations);
    }
}
