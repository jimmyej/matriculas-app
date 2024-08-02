package com.courses.controllers;

import com.courses.entities.Enrollment;
import com.courses.entities.EnrollmentCourse;
import com.courses.entities.ids.EnrollmentCourseId;
import com.courses.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/v1/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping(value = "")
    ResponseEntity<List<Enrollment>> getEnrollments(@RequestParam(required=false) String status){
        List<Enrollment> enrollments = enrollmentService.getEnrollments(status);
        if(enrollments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(enrollments);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id){
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        if(enrollment == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(enrollment);
    }

    @PostMapping(value = "")
    ResponseEntity<Enrollment> saveEnrollment(@RequestBody Enrollment enrollment){
        Enrollment newEnrollment = enrollmentService.saveEnrollment(enrollment);
        if(newEnrollment == null){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(newEnrollment, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<Enrollment> editEnrollment(@PathVariable Long id, @RequestBody Enrollment enrollment){
        Enrollment newEnrollment = enrollmentService.editEnrollment(id, enrollment);
        if(newEnrollment == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(newEnrollment);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Boolean> deleteEnrollment(@PathVariable Long id){
        boolean isDeleted = enrollmentService.deleteEnrollment(id);
        if(!isDeleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/dates/{enrollmentDate}")
    ResponseEntity<List<Enrollment>> getEnrollmentsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentDate){
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByDate(enrollmentDate);
        if(enrollments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(enrollments);
    }

    @GetMapping(value = "/details/{enrollmentId}/courses/{courseId}")
    ResponseEntity<EnrollmentCourse> getDetailById(@PathVariable Long enrollmentId, @PathVariable Long courseId) {
        EnrollmentCourseId id = new EnrollmentCourseId(enrollmentId, courseId);
        EnrollmentCourse enrollmentCourse = enrollmentService.getDetailById(id);
        if(enrollmentCourse == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(enrollmentCourse);
    }

    @PostMapping(value = "/details")
    ResponseEntity<EnrollmentCourse> saveDetail(@RequestBody EnrollmentCourse detail) {
        EnrollmentCourse enrollmentCourse = enrollmentService.saveDetail(detail);
        if(enrollmentCourse == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(enrollmentCourse);
    }

    @PostMapping(value = "/details/batch")
    ResponseEntity<List<EnrollmentCourse>> saveDetails(@RequestBody List<EnrollmentCourse> details) {
        List<EnrollmentCourse> enrollmentCourses = enrollmentService.saveDetails(details);
        if(enrollmentCourses.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(enrollmentCourses);
    }

    @DeleteMapping(value = "/details/{enrollmentId}/courses/{courseId}")
    ResponseEntity<Boolean> deleteDetailById(@PathVariable Long enrollmentId, @PathVariable Long courseId) {
        boolean isDeleted = enrollmentService.deleteDetailById(new EnrollmentCourseId(enrollmentId, courseId));
        if(!isDeleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
