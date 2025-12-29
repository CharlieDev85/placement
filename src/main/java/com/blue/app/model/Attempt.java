package com.blue.app.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    private String startedAt;
    private String completedAt;
    private BigDecimal scoreA1;
    private BigDecimal scoreA2;
    private BigDecimal scoreB1;
    private BigDecimal scoreB2;
    private Result result;

    public Attempt() {
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public BigDecimal getScoreA1() {
        return scoreA1;
    }

    public void setScoreA1(BigDecimal scoreBasic) {
        this.scoreA1 = scoreBasic;
    }

    public BigDecimal getScoreA2() {
        return scoreA2;
    }

    public void setScoreA2(BigDecimal scoreIntermediate) {
        this.scoreA2 = scoreIntermediate;
    }

    public BigDecimal getScoreB1() {
        return scoreB1;
    }

    public void setScoreB1(BigDecimal scoreAdvanced) {
        this.scoreB1 = scoreAdvanced;
    }

    public BigDecimal getScoreB2() {
        return scoreB2;
    }

    public void setScoreB2(BigDecimal scoreB2) {
        this.scoreB2 = scoreB2;
    }
}
