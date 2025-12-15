package com.blue.app.service;

import com.blue.app.model.Attempt;
import com.blue.app.model.Question;
import com.blue.app.model.Quiz;
import com.blue.app.model.Response;
import com.blue.app.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuestionService questionService;

    public QuizService(QuizRepository quizRepository, QuestionService questionService) {
        this.quizRepository = quizRepository;
        this.questionService = questionService;
    }

    public List<Quiz> get10Quizzes() {
       List<Quiz> quizzes = new ArrayList<>();
       for (int i = 0; i < 10; i++) {
           List<Question> questions = questionService.getRandomQuestions();
           Quiz quiz = new Quiz();
           quiz.setQuestions(questions);
           quizzes.add(quiz);
           quizRepository.save(quiz);
       }
       return quizzes;
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    public Quiz getRandomQuiz() {
        return quizRepository.findAll().get((int)(Math.random()*quizRepository.count()));
    }

    public void calculateScores(Attempt attempt, List<Response> responses) {
        int totalA1 = 0, correctA1 = 0;
        int totalA2 = 0, correctA2 = 0;
        int totalB1 = 0, correctB1 = 0;
        int totalB2 = 0, correctB2 = 0;

        for (Response response : responses) {
            String difficulty = response.getQuestion().getDifficulty();
            boolean isCorrect = response.isCorrect();

            switch (difficulty.toLowerCase()) {
                case "a1":
                    totalA1++;
                    if (isCorrect) correctA1++;
                    break;
                case "a2":
                    totalA2++;
                    if (isCorrect) correctA2++;
                    break;
                case "b1":
                    totalB1++;
                    if (isCorrect) correctB1++;
                    break;
                case "b2":
                    totalB2++;
                    if (isCorrect) correctB2++;
                    break;
            }
        }

        attempt.setScoreA1(calcRatio(correctA1, totalA1));
        attempt.setScoreA2(calcRatio(correctA2, totalA2));
        attempt.setScoreB1(calcRatio(correctB1, totalB1));
        attempt.setScoreB2(calcRatio(correctB2, totalB2));
    }

    private BigDecimal calcRatio(int correct, int total) {
        if (total == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(correct)
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }

}
