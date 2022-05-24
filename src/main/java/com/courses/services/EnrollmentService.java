package com.courses.services;

import com.courses.entities.Enrollment;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getEnrollments();
    Enrollment getEnrollmentById(Long id);
    Enrollment saveEnrollment(Enrollment enrollment);
    Enrollment editEnrollment(Long id, Enrollment enrollment);
    boolean deleteEnrollment(Long id);

    List<Enrollment> getEnrollmentsByDate(LocalDate enrollmentDate);

}
