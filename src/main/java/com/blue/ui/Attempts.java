package com.blue.ui;

import com.blue.app.model.Attempt;
import com.blue.app.service.AttemptService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Route("attempts")
@CssImport("./styles/quiz-view.css")
public class Attempts extends VerticalLayout {

    private final AttemptService attemptService;
    private final Grid<Attempt> attemptGrid = new Grid<>(Attempt.class, false);

    @Autowired
    public Attempts(AttemptService attemptService) {
        this.attemptService = attemptService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        configureGrid();
        loadAttempts();
        add(attemptGrid);
    }

    private void configureGrid() {
        attemptGrid.addColumn(attempt -> attempt.getStudent().getName())
                .setHeader("Student")
                .setAutoWidth(true);

        attemptGrid.addColumn(attempt -> attempt.getQuiz().getId())
                .setHeader("Quiz ID")
                .setAutoWidth(true);

        attemptGrid.addColumn(Attempt::getStartedAt)
                .setHeader("Started At")
                .setAutoWidth(true);

        attemptGrid.addColumn(Attempt::getCompletedAt)
                .setHeader("Completed At")
                .setAutoWidth(true);

        attemptGrid.addColumn(attempt -> formatPercent(attempt.getScoreA1()))
                .setHeader("A1")
                .setAutoWidth(true);

        attemptGrid.addColumn(attempt -> formatPercent(attempt.getScoreA2()))
                .setHeader("A2")
                .setAutoWidth(true);

        attemptGrid.addColumn(attempt -> formatPercent(attempt.getScoreB1()))
                .setHeader("B1")
                .setAutoWidth(true);

        attemptGrid.addColumn(attempt -> formatPercent(attempt.getScoreB2()))
                .setHeader("B2")
                .setAutoWidth(true);
    }

    private void loadAttempts() {
        List<Attempt> attempts = attemptService.getAllAttempts(); // Make sure this exists
        attemptGrid.setItems(attempts);
    }

    private String formatPercent(java.math.BigDecimal value) {
        if (value == null) return "-";
        return NumberFormat.getPercentInstance(Locale.US).format(value);
    }
}
