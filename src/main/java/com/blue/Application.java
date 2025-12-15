package com.blue;

import com.blue.app.service.QuestionService;
import com.blue.app.service.QuizService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Theme("default")
public class Application implements AppShellConfigurator {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner dataSeeder(QuestionService questionService, QuizService quizService) {
        return args -> {
            long questionCount = questionService.countQuestions();
            long quizCount = quizService.countQuizzes();

            if (questionCount == 0 && quizCount == 0) {
                log.info("Seeding database with questions and quizzes");
                questionService.getQuestionsFromExcelToDb();
                quizService.get10Quizzes();
                log.info("Seeding completed");
            } else {
                log.info("Skipping seeding: existing questions={} quizzes={}", questionCount, quizCount);
            }
        };
    }
}
