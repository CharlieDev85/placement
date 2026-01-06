package com.blue.app.repository;

import com.blue.app.model.EnrolledStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolledStudentRepository extends JpaRepository<EnrolledStudent, Long> {
}
