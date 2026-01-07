package com.blue.ui;

import com.blue.app.model.Question;
import com.blue.app.service.QuestionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Route("questions")
public class QuestionsView extends VerticalLayout {

    private final QuestionService questionService;
    private final Grid<Question> questionGrid = new Grid<>(Question.class, false);
    private GridListDataView<Question> dataView;
    private final TextField topicFilter = new TextField("Filter by topic");
    private final ComboBox<String> levelFilter = new ComboBox<>("Filter by CEFR level");

    @Autowired
    public QuestionsView(QuestionService questionService) {
        this.questionService = questionService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Questions"));
        add(buildHeaderControls());

        configureGrid();
        loadQuestions();

        add(questionGrid);
    }

    private HorizontalLayout buildHeaderControls() {
        Button addQuestionButton = new Button("Add New Question");
        addQuestionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addQuestionButton.addClickListener(event -> openQuestionDialog(new Question()));

        topicFilter.setClearButtonVisible(true);
        topicFilter.setValueChangeMode(ValueChangeMode.EAGER);
        topicFilter.addValueChangeListener(event -> applyFilters());

        levelFilter.setClearButtonVisible(true);
        levelFilter.setItems("A1", "A2", "B1", "B2");
        levelFilter.addValueChangeListener(event -> applyFilters());

        HorizontalLayout filters = new HorizontalLayout(topicFilter, levelFilter);
        filters.setAlignItems(Alignment.END);

        HorizontalLayout header = new HorizontalLayout(filters, addQuestionButton);
        header.setWidthFull();
        header.setAlignItems(Alignment.END);
        header.expand(filters);
        return header;
    }

    private void configureGrid() {
        questionGrid.addColumn(Question::getQuestion)
                .setHeader("Question")
                .setAutoWidth(true)
                .setFlexGrow(2);

        questionGrid.addColumn(Question::getOptionA)
                .setHeader("Option A")
                .setAutoWidth(true);
        questionGrid.addColumn(Question::getOptionB)
                .setHeader("Option B")
                .setAutoWidth(true);
        questionGrid.addColumn(Question::getOptionC)
                .setHeader("Option C")
                .setAutoWidth(true);
        questionGrid.addColumn(Question::getOptionD)
                .setHeader("Option D")
                .setAutoWidth(true);

        questionGrid.addColumn(Question::getCorrectAnswer)
                .setHeader("Right Answer")
                .setAutoWidth(true);

        questionGrid.addColumn(Question::getTopic)
                .setHeader("Topic")
                .setAutoWidth(true);

        questionGrid.addColumn(Question::getDifficulty)
                .setHeader("CEFR Level")
                .setAutoWidth(true);

        questionGrid.addComponentColumn(this::buildActions)
                .setHeader("Actions")
                .setAutoWidth(true);
    }

    private HorizontalLayout buildActions(Question question) {
        Button editButton = new Button("Modify");
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        editButton.addClickListener(event -> openQuestionDialog(question));

        Button deleteButton = new Button("Delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        deleteButton.addClickListener(event -> {
            questionService.deleteQuestion(question);
            Notification.show("Question deleted.");
            loadQuestions();
        });

        HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
        actions.setPadding(false);
        actions.setSpacing(true);
        return actions;
    }

    private void loadQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        dataView = questionGrid.setItems(questions);
        applyFilters();
    }

    private void applyFilters() {
        if (dataView == null) {
            return;
        }

        String topicValue = normalize(topicFilter.getValue());
        String levelValue = normalize(levelFilter.getValue());

        dataView.setFilter(question -> {
            boolean topicMatches = topicValue.isEmpty()
                    || normalize(question.getTopic()).contains(topicValue);
            boolean levelMatches = levelValue.isEmpty()
                    || normalize(question.getDifficulty()).equals(levelValue);
            return topicMatches && levelMatches;
        });
    }

    private String normalize(String value) {
        return Objects.requireNonNullElse(value, "").trim().toLowerCase(Locale.ROOT);
    }

    private void openQuestionDialog(Question question) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(question.getId() == null ? "Add Question" : "Modify Question");

        TextField questionField = new TextField("Question");
        TextField optionAField = new TextField("Option A");
        TextField optionBField = new TextField("Option B");
        TextField optionCField = new TextField("Option C");
        TextField optionDField = new TextField("Option D");
        TextField correctAnswerField = new TextField("Right Answer");
        TextField topicField = new TextField("Topic");
        ComboBox<String> levelField = new ComboBox<>("CEFR Level");
        levelField.setItems("A1", "A2", "B1", "B2");

        questionField.setValue(Objects.requireNonNullElse(question.getQuestion(), ""));
        optionAField.setValue(Objects.requireNonNullElse(question.getOptionA(), ""));
        optionBField.setValue(Objects.requireNonNullElse(question.getOptionB(), ""));
        optionCField.setValue(Objects.requireNonNullElse(question.getOptionC(), ""));
        optionDField.setValue(Objects.requireNonNullElse(question.getOptionD(), ""));
        correctAnswerField.setValue(Objects.requireNonNullElse(question.getCorrectAnswer(), ""));
        topicField.setValue(Objects.requireNonNullElse(question.getTopic(), ""));
        levelField.setValue(question.getDifficulty());

        FormLayout formLayout = new FormLayout(
                questionField,
                optionAField,
                optionBField,
                optionCField,
                optionDField,
                correctAnswerField,
                topicField,
                levelField
        );
        formLayout.setColspan(questionField, 2);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            question.setQuestion(questionField.getValue());
            question.setOptionA(optionAField.getValue());
            question.setOptionB(optionBField.getValue());
            question.setOptionC(optionCField.getValue());
            question.setOptionD(optionDField.getValue());
            question.setCorrectAnswer(correctAnswerField.getValue());
            question.setTopic(topicField.getValue());
            question.setDifficulty(levelField.getValue());
            questionService.saveQuestion(question);
            Notification.show("Question saved.");
            dialog.close();
            loadQuestions();
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
        dialog.add(formLayout);
        dialog.getFooter().add(actions);

        dialog.open();
    }
}
