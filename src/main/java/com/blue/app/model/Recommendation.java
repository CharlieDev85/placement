package com.blue.app.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private Result result;
    @Enumerated(EnumType.STRING)
    private Schedule scheduleChosen;
    @Enumerated(EnumType.STRING)
    private Course recommendedCourse;
    private LocalDate startingDate;
    private BigDecimal percentage;

    public Recommendation(Result result, Schedule scheduleChosen) {
        this.result = result;
        this.scheduleChosen = scheduleChosen;
    }
    public Recommendation(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Schedule getScheduleChosen() {
        return scheduleChosen;
    }

    public void setScheduleChosen(Schedule scheduleChosen) {
        this.scheduleChosen = scheduleChosen;
    }

    public Course getRecommendedCourse() {
        return recommendedCourse;
    }

    public void setRecommendedCourse(Course recommendedCourse) {
        this.recommendedCourse = recommendedCourse;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
