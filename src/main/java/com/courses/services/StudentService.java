package com.courses.services;

import com.courses.entities.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();
    Student getStudentById(Long id);
    Student saveStudent(Student student);
    Student editStudent(Long id, Student student);
    boolean deleteStudent(Long id);

    Student getStudentByDocNumber(String docNumber);
    List<Student> getStudentsByDocType(String docType);
}
