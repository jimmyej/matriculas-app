package com.courses.controllers;

import com.courses.entities.Student;
import com.courses.handlers.ResponseHandler;
import com.courses.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/v1/students")
public class StudentController {

    @Autowired
    StudentService studentService;

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
    ResponseEntity<Page<Student>> getAllStudents(
            @PathVariable(required = false) String filters,
            @PathVariable(required = false) String sorts,
            @PathVariable(value = "0") Integer page,
            @PathVariable(value = "10") Integer size){

        Page<Student> students =  studentService.getAllStudents(filters, sorts, page, size);
        if(students.get().findAny().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping(value = "")
    ResponseEntity<Object> getStudents(@RequestParam(required=false) String status){
        List<Student> students = studentService.getStudents(status);
        if(students.isEmpty()){
            return ResponseHandler.generateResponse(students, "Users no found", HttpStatus.NO_CONTENT);
        }
        return ResponseHandler.generateResponse(students, "Getting users successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Student> getStudentById(@PathVariable Long id){
        Student student = studentService.getStudentById(id);
        if(student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(student);
    }

    @PostMapping(value = "")
    ResponseEntity<Student> saveStudent(@RequestBody Student student){
        Student newStudent = studentService.saveStudent(student);
        if(newStudent == null){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<Student> editStudent(@PathVariable Long id, @RequestBody Student student){
        Student newStudent = studentService.editStudent(id, student);
        if(newStudent == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(newStudent);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Boolean> deleteStudent(@PathVariable Long id){
        boolean isDeleted = studentService.deleteStudent(id);
        if(!isDeleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/docs/numbers/{docNumber}")
    ResponseEntity<Student> getStudentByDocNumber(@PathVariable String docNumber){
        Student student = studentService.getStudentByDocNumber(docNumber);
        if(student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(student);
    }

    @GetMapping(value = "/docs/types/{docType}")
    ResponseEntity<List<Student>> getStudentsByDocType(@PathVariable String docType){
        List<Student> students = studentService.getStudentsByDocType(docType);
        if(students.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(students);
    }

    @PostMapping(value = {"/{id}/upload", "/{id}/upload/{publicId}"})
    ResponseEntity<Student> uploadPhoto(@PathVariable Long id, @PathVariable(required = false) String publicId, @RequestParam MultipartFile file){
        Student student = studentService.uploadPhoto(id, file, publicId);
        if(student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(student);
    }
}
