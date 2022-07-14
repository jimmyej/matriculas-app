package com.courses.controllers;

import com.courses.entities.Student;
import com.courses.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(
        value = {
            "/{sorts}/{page}/{size}",
            "/{filters}/{page}/{size}",
            "/{page}/{size}",
            "/{filters}/{sorts}/{page}/{size}"
        },
        consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }
    )
    Page<Student> getAllStudents(@PathVariable(required = false) String filters, @PathVariable(required = false) String sorts, @PathVariable Integer page, @PathVariable Integer size){
        return studentService.getAllStudents(filters, sorts, page, size);
    }

    @GetMapping(value = "")
    List<Student> getStudents(@RequestParam(required=false) boolean showAll){
        return showAll ? studentService.getStudents() : studentService.getActiveStudents();
    }

    @GetMapping(value = "/{id}")
    Student getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @PostMapping(value = "")
    Student saveStudent(@RequestBody Student student){
        return studentService.saveStudent(student);
    }

    @PutMapping(value = "/{id}")
    Student editStudent(@PathVariable Long id, @RequestBody Student student){
        return studentService.editStudent(id, student);
    }

    @DeleteMapping(value = "/{id}")
    boolean deleteStudent(@PathVariable Long id){
        return studentService.deleteStudent(id);
    }

    @GetMapping(value = "/docs/numbers/{docNumber}")
    Student getStudentByDocNumber(@PathVariable String docNumber){
        return studentService.getStudentByDocNumber(docNumber);
    }

    @GetMapping(value = "/docs/types/{docType}")
    List<Student> getStudentsByDocType(@PathVariable String docType){
        return studentService.getStudentsByDocType(docType);
    }

    @PostMapping(value = {"/{id}/upload", "/{id}/upload/{publicId}"})
    Student uploadPhoto(@PathVariable Long id, @PathVariable(required = false) String publicId,@RequestParam MultipartFile file){
        return studentService.uploadPhoto(id, file, publicId);
    }
}
