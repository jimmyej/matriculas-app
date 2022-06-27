package com.courses.services.impls;

import com.courses.entities.Student;
import com.courses.repositories.StudentRepository;
import com.courses.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
        //return studentRepository.findByStatus(true);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student editStudent(Long id, Student student) {
        boolean exists = studentRepository.existsById(id);
        if(exists){
            return studentRepository.save(student);
        }  else {
            return null;
        }
    }

    public boolean deleteStudent(Long id) {
        boolean deleted = false;
        if(studentRepository.existsById(id)){
            Student student = studentRepository.findById(id).get();
            student.setStatus(false);
            studentRepository.save(student);
            deleted = true;
        }
        return deleted;
    }

    public Student getStudentByDocNumber(String docNumber) {
        return studentRepository.findByDocNumber(docNumber);
    }

    public List<Student> getStudentsByDocType(String docType) {
        return studentRepository.findByDocType(docType);
    }
}
