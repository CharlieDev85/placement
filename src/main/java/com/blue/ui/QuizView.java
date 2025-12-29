package com.blue.ui;

import com.blue.app.model.*;
import com.blue.app.service.AttemptService;
import com.blue.app.service.QuizService;
import com.blue.app.service.ResponseService;
import com.blue.app.session.AttemptSession;
import com.blue.app.session.StudentSession;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route("quiz")
@CssImport("./styles/quiz-view.css")
public class QuizView extends VerticalLayout {
    private final StudentSession studentSession;
    private final AttemptSession attemptSession;
    private final QuizService quizService;

    private int currentQuestion = 0;
    private int totalQuestions = 30;
    private Quiz quiz;
    private H2 questionTitle;
    private Question question;
    private List<Response> responses = new ArrayList<>();
    // UI components you’ll update dynamically
    private Span questionText;
    private RadioButtonGroup<String> options;
    //ui elements
    private Button nextBtn;
    private Button finishBtn;
    private final ProgressBar progressBar;

    //
    private UI ui;

    @Autowired
    private AttemptService attemptService;
    private Attempt attempt;
    @Autowired
    private ResponseService responseService;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        if (studentSession.getStudent() != null && quiz != null) {
            // Only create the Attempt once per session/view load
            attempt = attemptService.createAttempt(studentSession.getStudent(), quiz);
        }
    }


    @Autowired
    public QuizView(QuizService quizService, StudentSession studentSession, AttemptSession attemptSession) {
        this.quizService = quizService;
        this.studentSession = studentSession;
        this.attemptSession = attemptSession;
        //todo: add timer


        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassName("quiz-background");

        //get Student from Session
        Student student = this.studentSession.getStudent();
        Span studentInfo = new Span("👤 " + student.getName() + " | ✉️ " + student.getEmail());


        // Load 30 random questions
        //this.questions = questionService.get30RandomQuestions();
        //private final List<Question> questions;
        quiz = this.quizService.getRandomQuiz();
        totalQuestions = quiz.getQuestions().size();

        // Card container
        Div card = new Div();
        card.addClassName("quiz-card");

        // Question header
        questionTitle = new H2("Question Base");
        questionText = new Span();
        questionText.addClassName("quiz-question");

        // Options
        options = new RadioButtonGroup<>();
        //options.setItems("Sad", "Glad", "Mad", "Bad");
        options.addClassName("quiz-options");
        options.setThemeName("vertical"); // <-- This is the key line


        // Centered Button Container
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();

        // Next button
        nextBtn = new Button("Next", new Icon(VaadinIcon.ARROW_RIGHT));
        nextBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextBtn.addClickListener(e -> nextQuestion());
        nextBtn.setVisible(true);
        buttonLayout.add(nextBtn);

        //Finish button
        finishBtn = new Button("Finish", new Icon(VaadinIcon.CHECK));
        finishBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        finishBtn.addClickListener(e -> finishTest());
        finishBtn.setVisible(false);
        buttonLayout.add(finishBtn);

        /*
        nextBtn.addClickListener(e -> {
            Notification.show("Answer submitted!");
            // TODO: Advance to next question
        });
         */

        //showQuestion(0);
        card.add(questionTitle, questionText, options, buttonLayout);

        //Progress Bar
        progressBar = new ProgressBar(0, 40, 0);
        progressBar.setWidth("80%");

        showQuestion(currentQuestion);//updates the card with the first question

        add(studentInfo);
        add(progressBar);
        add(card);

    }

    private void showQuestion(int index) {

        //quiz finished
        if(index >= totalQuestions){
            questionTitle.setText("Quiz Finished 🎉");
            questionText.setText("You've completed the quiz!");
            options.setItems();
            nextBtn.setVisible(false);
            finishBtn.setVisible(true);
            progressBar.setValue(totalQuestions);
            return;
        }

        question = quiz.getQuestions().get(index);
        //remove the question number text
        //questionTitle.setText("Question " + (index + 1));
        questionText.setText(question.getQuestion());
        options.setItems(question.getOptionA(), question.getOptionB(), question.getOptionC(), question.getOptionD());
        options.clear();
        progressBar.setValue(index + 1);
    }

    private void nextQuestion() {
        if (options.getValue() == null) {
            Notification.show("Please select an answer before proceeding!");
            return;
        }
        saveResponse();
        // You could store the selected answer here if needed (e.g. in a map)
        // String selectedAnswer = options.getValue();
        currentQuestion++;
        showQuestion(currentQuestion);
    }

    private void saveResponse(){
        Response response = new Response();
        response.setStudent(studentSession.getStudent());
        response.setAttempt(attempt);
        response.setQuestion(quiz.getQuestions().get(currentQuestion));
        response.setAnswer(options.getValue());
        response.setCorrect(isResponseCorrect());
        this.responses.add(response);
        responseService.save(response);
    }

    private boolean isResponseCorrect(){
        String correctAnswer = switch (question.getCorrectAnswer()) {
            case "A" -> question.getOptionA();
            case "B" -> question.getOptionB();
            case "C" -> question.getOptionC();
            case "D" -> question.getOptionD();
            default -> "";
        };
        return correctAnswer.equals(options.getValue());
    }

    private void finishTest(){
        completeAttempt();
        finishBtn.getUI().ifPresent(ui ->
                ui.navigate("test-completed"));

    }

    private void completeAttempt(){
        attempt.setCompletedAt(LocalDateTime.now().toString());
        quizService.calculateScores(attempt, responses);
        attemptSession.setAttempt(attempt);
        attemptService.save(attempt);
    }


}


