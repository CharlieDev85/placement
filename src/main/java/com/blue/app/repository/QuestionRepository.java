package com.blue.app.repository;

import com.blue.app.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM question WHERE difficulty = 'Basic' ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Question> find10RandomBasicQuestions();
    @Query(value = "SELECT * FROM question WHERE difficulty = 'Intermediate' ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Question> find10RandomIntermediateQuestions();
    @Query(value = "SELECT * FROM question WHERE difficulty = 'Advanced' ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Question> find10RandomAdvancedQuestions();

    @Query(value = "SELECT * FROM question WHERE difficulty = 'A1' ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Question> find10RandomA1Questions();
    @Query(value = "SELECT * FROM question WHERE difficulty = 'A2' ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Question> find10RandomA2Questions();
    @Query(value = "SELECT * FROM question WHERE difficulty = 'B1' ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Question> find10RandomB1Questions();
    @Query(value = "SELECT * FROM question WHERE difficulty = 'B2' ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Question> find10RandomB2Questions();

}
