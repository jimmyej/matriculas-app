package com.courses.controllers;

import com.courses.entities.Enrollment;
import com.courses.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping(value = "")
    List<Enrollment> getEnrollments(){
        return enrollmentService.getEnrollments();
    }

    @GetMapping(value = "/{id}")
    Enrollment getEnrollmentById(@PathVariable Long id){
        return enrollmentService.getEnrollmentById(id);
    }

    @PostMapping(value = "")
    Enrollment saveEnrollment(@RequestBody Enrollment enrollment){
        return enrollmentService.saveEnrollment(enrollment);
    }

    @PutMapping(value = "/{id}")
    Enrollment editEnrollment(@PathVariable Long id, @RequestBody Enrollment enrollment){
        return enrollmentService.editEnrollment(id, enrollment);
    }

    @DeleteMapping(value = "/{id}")
    boolean deleteEnrollment(@PathVariable Long id){
        return enrollmentService.deleteEnrollment(id);
    }

    @GetMapping(value = "/dates/{enrollmentDate}")
    List<Enrollment> getEnrollmentsByDate(@PathVariable LocalDate enrollmentDate){
        return enrollmentService.getEnrollmentsByDate(enrollmentDate);
    }
}
