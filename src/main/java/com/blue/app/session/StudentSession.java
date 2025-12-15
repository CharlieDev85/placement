package com.blue.app.session;

import com.blue.app.model.Student;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class StudentSession {
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isStudentSet() {
        return student != null;
    }

    public void clear() {
        this.student = null;
    }
}
