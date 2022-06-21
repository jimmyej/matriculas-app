package com.courses.repositories;

import com.courses.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByDocNumber(String docNumber);
    List<Student> findByDocType(String docType);
    List<Student> findByStatus(boolean status);
}
