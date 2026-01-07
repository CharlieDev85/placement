package com.blue.ui;

import com.blue.app.model.Course;
import com.blue.app.model.Recommendation;
import com.blue.app.model.Result;
import com.blue.app.model.Schedule;
import com.blue.app.repository.RecommendationRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Route("recommendations")
public class RecommendationView extends VerticalLayout {

    private final RecommendationRepository recommendationRepository;
    private final Grid<Recommendation> grid = new Grid<>(Recommendation.class, false);
    private final Dialog dialog = new Dialog();
    private final DateTimeFormatter formatter;

    private Recommendation currentRecommendation;

    //filters
    private TextField resultFilter = new TextField();
    private ComboBox<Schedule> scheduleFilter = new ComboBox<>();
    private ComboBox<Schedule> recommendedScheduleFilter = new ComboBox<>();
    private ComboBox<Course> courseFilter = new ComboBox<>();

    @Autowired
    public RecommendationView(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
        setSizeFull();
        configureGrid();
        loadGridData();

        formatter = DateTimeFormatter.ofPattern("E MMM d, yyyy", Locale.ENGLISH);



        Button addBtn = new Button("New recommendation", event -> openEditor(new Recommendation()));
        addBtn.getStyle().set("margin-top", "20px");
        add(addBtn);
    }

    private void configureGrid() {
        grid.setWidthFull();

        //filters
        resultFilter = new TextField();
        resultFilter.setPlaceholder("Filter by Result");
        scheduleFilter = new ComboBox<>();
        scheduleFilter.setPlaceholder("Filter by Schedule Chosen");
        scheduleFilter.setItems(Schedule.values());
        scheduleFilter.setItemLabelGenerator(Schedule::getDisplayName);
        recommendedScheduleFilter = new ComboBox<>();
        recommendedScheduleFilter.setPlaceholder("Filter by Recommended Schedule");
        recommendedScheduleFilter.setItems(Schedule.values());
        recommendedScheduleFilter.setItemLabelGenerator(Schedule::getDisplayName);
        courseFilter = new ComboBox<>();
        courseFilter.setPlaceholder("Filter by Course");
        courseFilter.setItems(Course.values());
        courseFilter.setItemLabelGenerator(Course::getDisplayName);
        HorizontalLayout filters = new HorizontalLayout(resultFilter, scheduleFilter, recommendedScheduleFilter, courseFilter);

        resultFilter.addValueChangeListener(e -> applyFilters());
        scheduleFilter.addValueChangeListener(e -> applyFilters());
        recommendedScheduleFilter.addValueChangeListener(e -> applyFilters());
        courseFilter.addValueChangeListener(e -> applyFilters());


        grid.addColumn(Recommendation::getResult).setHeader("Result");
        grid.addColumn(r -> r.getScheduleChosen() != null ? r.getScheduleChosen().getDisplayName() : "").setHeader("Schedule chosen");
        grid.addColumn(r -> r.getRecommendedCourse() != null ? r.getRecommendedCourse().getDisplayName() : "").setHeader("Course");
        grid.addColumn(r -> r.getRecommendedSchedule() != null ? r.getRecommendedSchedule().getDisplayName() : "").setHeader("Recommended Schedule");

        grid.addColumn(r -> {
            LocalDate date = r.getStartingDate();
            return date != null ? date.format(formatter) : "";
        }).setHeader("Starting Date");
        grid.addColumn(r -> {
            BigDecimal percent = r.getPercentage();
            return percent != null ? (percent.multiply(BigDecimal.valueOf(100)).setScale(0)) + "%" : "";
        }).setHeader("Percentage");

        grid.addComponentColumn(recommendation -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT), click -> openEditor(recommendation));
            edit.addClassName("edit-button");
            return edit;
        }).setHeader("Edit");

        add(filters); // Add this above the grid
        add(grid);
    }

    private void loadGridData() {
        /*
        List<Recommendation> recommendations = recommendationRepository.findAll();
        grid.setItems(recommendations);
         */
        applyFilters();

    }

    private void openEditor(Recommendation recommendation) {
        currentRecommendation = recommendation;

        ComboBox<Result> resultCombo = new ComboBox<>("Result");
        resultCombo.setItems(Result.values());
        resultCombo.setItemLabelGenerator(Result::getDisplayName);
        resultCombo.setValue(recommendation.getResult());

        ComboBox<Schedule> scheduleCombo = new ComboBox<>("Schedule");
        scheduleCombo.setItems(Schedule.values());
        scheduleCombo.setItemLabelGenerator(Schedule::getDisplayName);
        scheduleCombo.setValue(recommendation.getScheduleChosen());



        ComboBox<Course> courseCombo = new ComboBox<>("Course", Course.values());
        courseCombo.setItemLabelGenerator(Course::getDisplayName);
        courseCombo.setItems(Course.values());
        courseCombo.setValue(recommendation.getRecommendedCourse());

        ComboBox<Schedule> recommendedScheduleCombo = new ComboBox<>("Recommended Schedule");
        recommendedScheduleCombo.setItems(Schedule.values());
        recommendedScheduleCombo.setItemLabelGenerator(Schedule::getDisplayName);
        recommendedScheduleCombo.setValue(recommendation.getRecommendedSchedule());

        DatePicker startDateField = new DatePicker("Starting Date");
        startDateField.setLocale(Locale.ENGLISH);
        startDateField.setValue(recommendation.getStartingDate());
        startDateField.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                String formatted = selectedDate.format(formatter);
                Notification.show("Selected date: " + formatted);
            }
        });

        NumberField percentageField = new NumberField("Percentage");
        percentageField.setStep(1);
        percentageField.setMin(0);
        percentageField.setMax(100);
        if (recommendation.getPercentage() != null) {
            percentageField.setValue(recommendation.getPercentage().multiply(BigDecimal.valueOf(100)).doubleValue());
        }
        // Add the % symbol inside the field
        percentageField.setSuffixComponent(new Span("%"));

        Button save = new Button("Save", event -> {


            recommendation.setResult(resultCombo.getValue());
            recommendation.setScheduleChosen(scheduleCombo.getValue());
            recommendation.setRecommendedSchedule(recommendedScheduleCombo.getValue());
            recommendation.setRecommendedCourse(courseCombo.getValue());
            recommendation.setStartingDate(startDateField.getValue());
            Double input = percentageField.getValue();

            try {
                recommendation.setPercentage(new BigDecimal(percentageField.getValue()));
            } catch (NumberFormatException e) {
                recommendation.setPercentage(BigDecimal.ZERO);
            }
            if (input != null) {
                recommendation.setPercentage(BigDecimal.valueOf(input).divide(BigDecimal.valueOf(100)));
            }
            recommendationRepository.save(recommendation);
            dialog.close();
            loadGridData();
        });

        Button delete = new Button("Delete", event -> {
            if (recommendation.getId() != null) {
                recommendationRepository.delete(recommendation);
                dialog.close();
                loadGridData();
            }
        });

        Button cancel = new Button("Cancel", e -> dialog.close());

        HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
        VerticalLayout layout = new VerticalLayout(resultCombo, scheduleCombo, recommendedScheduleCombo, courseCombo, startDateField, percentageField, actions);
        dialog.removeAll();
        dialog.add(layout);
        dialog.open();
    }

    private void applyFilters() {
        List<Recommendation> all = recommendationRepository.findAll();
        List<Recommendation> filtered = all.stream()
                .filter(rec -> resultFilter.isEmpty() ||
                        (rec.getResult() != null && rec.getResult().name().toLowerCase().contains(resultFilter.getValue().toLowerCase())))
                .filter(rec -> scheduleFilter.isEmpty() || rec.getScheduleChosen() == scheduleFilter.getValue())
                .filter(rec -> recommendedScheduleFilter.isEmpty() || rec.getRecommendedSchedule() == recommendedScheduleFilter.getValue())
                .filter(rec -> courseFilter.isEmpty() || rec.getRecommendedCourse() == courseFilter.getValue())
                .toList();

        grid.setItems(filtered);
    }

}
