package com.blue.app.service;

import com.blue.app.model.Question;
import com.blue.app.repository.QuestionRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getQuestionsFromCsvToDb() {
        List<Question> questions = new ArrayList<>();
        try {
            // Load file from resources
            ClassPathResource resource = new ClassPathResource("questions/Generated Questions.csv");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                // Skip header if exists
                String line = reader.readLine();

                // Read each line
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");

                    // Basic CSV parsing — trim to avoid spaces
                    Question q = new Question();
                    //q.setId(Long.parseLong(fields[0].trim()));
                    q.setQuestion(fields[1].trim());
                    q.setOptionA(fields[2].trim());
                    q.setOptionB(fields[3].trim());
                    q.setOptionC(fields[4].trim());
                    q.setOptionD(fields[5].trim());
                    q.setCorrectAnswer(fields[6].trim());
                    q.setTopic(fields[7].trim());
                    q.setDifficulty(fields[8].trim());
                    questionRepository.save(q);
                    questions.add(q);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠️ Error saving question: " + e.getMessage());

        }

        return questions;
    }

    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

    public long countQuestions() {
        return questionRepository.count();
    }

    public List<Question> getRandomQuestions(){
        /*
        List<Question> basic = questionRepository.find10RandomBasicQuestions();
        List<Question> intermediate = questionRepository.find10RandomIntermediateQuestions();
        List<Question> advanced = questionRepository.find10RandomAdvancedQuestions();
          */
        List<Question> A1Questions = questionRepository.find10RandomA1Questions();
        List<Question> A2Questions = questionRepository.find10RandomA2Questions();
        List<Question> B1Questions = questionRepository.find10RandomB1Questions();
        List<Question> B2Questions = questionRepository.find10RandomB2Questions();

        List<Question> combined = new ArrayList<>();
        combined.addAll(A1Questions);
        combined.addAll(A2Questions);
        combined.addAll(B1Questions);
        combined.addAll(B2Questions);
        Collections.shuffle(combined);
        return combined;
    }

    public List<Question> getQuestionsFromExcelToDb() {
        List<Question> questions = new ArrayList<>();

        try {
            ClassPathResource resource = new ClassPathResource("questions/AI_Generated_Questions_V3.xlsx");
            InputStream inputStream = resource.getInputStream();

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            boolean skipHeader = true;
            for (Row row : sheet) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                Question q = new Question();
                q.setQuestion(getCellValue(row, 1));
                q.setOptionA(getCellValue(row, 2));
                q.setOptionB(getCellValue(row, 3));
                q.setOptionC(getCellValue(row, 4));
                q.setOptionD(getCellValue(row, 5));
                q.setCorrectAnswer(getCellValue(row, 6));
                q.setTopic(getCellValue(row, 7));
                q.setDifficulty(getCellValue(row, 8));

                questionRepository.save(q);
                questions.add(q);
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠️ Error saving question from Excel: " + e.getMessage());
        }

        return questions;
    }

    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}