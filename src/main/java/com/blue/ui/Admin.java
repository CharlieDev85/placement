package com.blue.ui;

import com.blue.app.service.QuestionService;
import com.blue.app.service.QuizService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("admin")
public class Admin extends VerticalLayout {


    @Autowired
    public Admin(QuestionService questionService, QuizService quizService) {
        Button getQuestionsBtn = new Button("Save  300 questions in DB", VaadinIcon.ARROW_RIGHT.create());
        getQuestionsBtn.addClickListener(e -> {
            //var questions = questionService.getQuestionsFromCsvToDb();
            var questions = questionService.getQuestionsFromExcelToDb();
            Notification.show("Questions fetched successfully!"+ " Amount: " + questions.size() + ".");
        });
        add(getQuestionsBtn);

        Button get10Quizzes = new Button("Save 10 Quizzes in DB", VaadinIcon.ARROW_RIGHT.create());
        get10Quizzes.addClickListener(e -> {
            var quizzes = quizService.get10Quizzes();
            Notification.show("10 Quizzes saved succesfully"+ " Amount: " + quizzes.size() + ".");
        });
        add(get10Quizzes);

    }
}


