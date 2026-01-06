package com.blue;

import com.blue.app.service.QuestionService;
import com.blue.app.service.QuizService;
import com.blue.app.service.RecommendationService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Theme("default")
@Push
public class Application implements AppShellConfigurator {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    //@Profile("dev")
    public CommandLineRunner dataSeeder(QuestionService questionService, QuizService quizService, RecommendationService recommendationService) {
        return args -> {
            long questionCount = questionService.countQuestions();
            long quizCount = quizService.countQuizzes();
            long recommendationCount = recommendationService.countRecommendations();

            if (questionCount == 0 && quizCount == 0) {
                log.info("Seeding database with questions and quizzes");
                questionService.getQuestionsFromExcelToDb();
                quizService.get10Quizzes();
                log.info("Seeding completed");
            } else {
                log.info("Skipping seeding: existing questions={} quizzes={}", questionCount, quizCount);
            }

            if(recommendationCount == 0){
                log.info("Seeding database with recommendations");
                recommendationService.seed();
            }
        };
    }
}
