package com.blue.app.service;

import com.blue.app.model.Student;
import com.blue.app.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void save(Student student){
        studentRepository.save(student);
    }

}
