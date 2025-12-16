package com.blue.app.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;
    private String result;
    @Enumerated(EnumType.STRING)
    private Schedule scheduleChosen;
    @Enumerated(EnumType.STRING)
    private Course recommendedCourse;
    private String startingDate;
    private BigDecimal percentage;

    public Recommendation(String result, Schedule scheduleChosen) {
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
