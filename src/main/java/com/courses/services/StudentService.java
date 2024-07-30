package com.courses.services;

import com.courses.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    Page<Student> getAllStudents(String filters, String sorts, Integer page, Integer size);
    List<Student> getStudents();
    List<Student> getActiveStudents();
    Student getStudentById(Long id);
    Student saveStudent(Student student);
    Student editStudent(Long id, Student student);
    boolean deleteStudent(Long id);

    Student getStudentByDocNumber(String docNumber);
    List<Student> getStudentsByDocType(String docType);

    Student uploadPhoto(Long id, MultipartFile image, String token);
}
