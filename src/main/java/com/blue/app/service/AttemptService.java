package com.blue.app.service;

import com.blue.app.model.Attempt;
import com.blue.app.model.Quiz;
import com.blue.app.model.Student;
import com.blue.app.repository.AttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class AttemptService {
    @Autowired
    private AttemptRepository attemptRepository;

    public Attempt createAttempt(Student student, Quiz quiz) {
        Attempt attempt = new Attempt();
        attempt.setStudent(student);
        attempt.setQuiz(quiz);
        attempt.setStartedAt(LocalDateTime.now().toString());
        return attemptRepository.save(attempt);
    }

    public Attempt save(Attempt attempt){
        return attemptRepository.save(attempt);
    }

    public List<Attempt> getAllAttempts(){
        return attemptRepository.findAll();
    }
}
