package com.blue.app.service;

import com.blue.app.model.EnrolledStudent;
import com.blue.app.repository.EnrolledStudentRepository;
import org.springframework.stereotype.Service;

@Service
public class EnrolledStudentService {
    private final EnrolledStudentRepository enrolledStudentRepository;

    public EnrolledStudentService(EnrolledStudentRepository enrolledStudentRepository) {
        this.enrolledStudentRepository = enrolledStudentRepository;
    }

    public EnrolledStudent save(EnrolledStudent enrolledStudent) {
        return enrolledStudentRepository.save(enrolledStudent);
    }
}
