package com.blue.app.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RecommendationMade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_made_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private Attempt attempt;
    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;
    private LocalDateTime createdAt;

    public RecommendationMade() {
    }

    public RecommendationMade(Attempt attempt, Recommendation recommendation) {
        this.attempt = attempt;
        this.recommendation = recommendation;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    public void setAttempt(Attempt attempt) {
        this.attempt = attempt;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
