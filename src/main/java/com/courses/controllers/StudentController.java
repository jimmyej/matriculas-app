package com.courses.controllers;

import com.courses.entities.Student;
import com.courses.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @ResponseBody
    ResponseEntity<Page<Student>> getAllStudents(@PathVariable(required = false) String filters, @PathVariable(required = false) String sorts, @PathVariable Integer page, @PathVariable Integer size){
        Page<Student> students =  studentService.getAllStudents(filters, sorts, page, size);
        if(students.get().findAny().isEmpty()){
            return new ResponseEntity<>(students, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping(value = "")
    ResponseEntity<List<Student>> getStudents(@RequestParam(required=false) boolean showAll){
        List<Student> students = showAll ? studentService.getStudents() : studentService.getActiveStudents();
        if(students.isEmpty()){
            return new ResponseEntity<>(students, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Student> getStudentById(@PathVariable Long id){
        Student student = studentService.getStudentById(id);
        if(student == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    //TODO refactor this endpoint
    @PostMapping(value = "")
    ResponseEntity<Student> saveStudent(@RequestBody Student student){
        Student newStudent = studentService.saveStudent(student);
        if(newStudent == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    //TODO refactor this endpoint
    @PutMapping(value = "/{id}")
    ResponseEntity<Student> editStudent(@PathVariable Long id, @RequestBody Student student){
        Student newStudent = studentService.editStudent(id, student);
        if(newStudent == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newStudent, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Boolean> deleteStudent(@PathVariable Long id){
        boolean isDeleted = studentService.deleteStudent(id);
        if(!isDeleted){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/docs/numbers/{docNumber}")
    ResponseEntity<Student> getStudentByDocNumber(@PathVariable String docNumber){
        Student student = studentService.getStudentByDocNumber(docNumber);
        if(student == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(value = "/docs/types/{docType}")
    ResponseEntity<List<Student>> getStudentsByDocType(@PathVariable String docType){
        List<Student> students = studentService.getStudentsByDocType(docType);
        if(students.isEmpty()){
            return new ResponseEntity<>(students, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping(value = {"/{id}/upload", "/{id}/upload/{publicId}"})
    ResponseEntity<Student> uploadPhoto(@PathVariable Long id, @PathVariable(required = false) String publicId, @RequestParam MultipartFile file){
        Student student = studentService.uploadPhoto(id, file, publicId);
        if(student == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(student, HttpStatus.OK);
    }
}
