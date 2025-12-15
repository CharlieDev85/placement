package com.blue.app.model;

import jakarta.persistence.*;

import java.util.List;

@Entity

public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;
    private String name;
    private String email;
    private String whatsappNum;
    private List<String> schedulesPref;
    private String ageRange;

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSchedulesPref() {
        return schedulesPref;
    }

    public void setSchedulesPref(List<String> schedulesPref) {
        this.schedulesPref = schedulesPref;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String dateRange) {
        this.ageRange = dateRange;
    }

    public String getWhatsappNum() {
        return whatsappNum;
    }

    public void setWhatsappNum(String whatsappNum) {
        this.whatsappNum = whatsappNum;
    }
}
