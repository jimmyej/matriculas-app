package com.courses.controllers;

import com.courses.entities.Student;
import com.courses.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping(value = "")
    @ResponseBody List<Student> getStudents(){
        return studentService.getStudents();
    }

    @GetMapping(value = "/{id}")
    @ResponseBody Student getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @PostMapping(value = "")
    @ResponseBody Student saveStudent(@RequestBody Student student){
        return studentService.saveStudent(student);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody Student editStudent(@PathVariable Long id, @RequestBody Student student){
        return studentService.editStudent(id, student);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody boolean deleteStudent(@PathVariable Long id){
        return studentService.deleteStudent(id);
    }

    @GetMapping(value = "/docs/numbers/{docNumber}")
    @ResponseBody Student getStudentByDocNumber(@PathVariable String docNumber){
        return studentService.getStudentByDocNumber(docNumber);
    }

    @GetMapping(value = "/docs/types/{docType}")
    @ResponseBody List<Student> getStudentsByDocType(@PathVariable String docType){
        return studentService.getStudentsByDocType(docType);
    }
}
