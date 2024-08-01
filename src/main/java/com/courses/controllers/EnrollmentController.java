package com.courses.controllers;

import com.courses.entities.Enrollment;
import com.courses.entities.EnrollmentCourse;
import com.courses.entities.ids.EnrollmentCourseId;
import com.courses.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    List<Enrollment> getEnrollmentsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enrollmentDate){
        return enrollmentService.getEnrollmentsByDate(enrollmentDate);
    }

    @GetMapping(value = "/details/{enrollmentId}/courses/{courseId}")
    EnrollmentCourse getDetailById(@PathVariable Long enrollmentId, @PathVariable Long courseId) {
        EnrollmentCourseId id = new EnrollmentCourseId(enrollmentId, courseId);
        return enrollmentService.getDetailById(id);
    }

    @PostMapping(value = "/details")
    EnrollmentCourse saveDetail(@RequestBody EnrollmentCourse detail) {
        return enrollmentService.saveDetail(detail);
    }

    @PostMapping(value = "/details/batch")
    List<EnrollmentCourse> saveDetails(@RequestBody List<EnrollmentCourse> details) {
        return enrollmentService.saveDetails(details);
    }

    @DeleteMapping(value = "/details/{enrollmentId}/courses/{courseId}")
    boolean deleteDetailById(@PathVariable Long enrollmentId, @PathVariable Long courseId) {
        return enrollmentService.deleteDetailById(new EnrollmentCourseId(enrollmentId, courseId));
    }
}
