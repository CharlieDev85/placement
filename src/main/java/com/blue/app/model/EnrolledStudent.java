package com.blue.app.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class EnrolledStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrolled_student_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @Enumerated(EnumType.STRING)
    private Course course;
    @Enumerated(EnumType.STRING)
    private Schedule recommendedSchedule;
    private LocalDate startDate;
    private LocalDateTime createdAt;

    public EnrolledStudent() {
    }

    public EnrolledStudent(Student student, Course course, Schedule recommendedSchedule, LocalDate startDate) {
        this.student = student;
        this.course = course;
        this.recommendedSchedule = recommendedSchedule;
        this.startDate = startDate;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Schedule getRecommendedSchedule() {
        return recommendedSchedule;
    }

    public void setRecommendedSchedule(Schedule recommendedSchedule) {
        this.recommendedSchedule = recommendedSchedule;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
