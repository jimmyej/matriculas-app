package com.courses.services;

import com.courses.entities.Enrollment;
import com.courses.entities.EnrollmentCourse;
import com.courses.entities.ids.EnrollmentCourseId;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getEnrollments(String status);
    Enrollment getEnrollmentById(Long id);
    Enrollment saveEnrollment(Enrollment enrollment);
    Enrollment editEnrollment(Long id, Enrollment enrollment);
    boolean deleteEnrollment(Long id);
    List<Enrollment> getEnrollmentsByDate(LocalDate enrollmentDate);

    EnrollmentCourse getDetailById(EnrollmentCourseId id);
    EnrollmentCourse saveDetail(EnrollmentCourse detail);
    List<EnrollmentCourse> saveDetails(List<EnrollmentCourse> details);
    boolean deleteDetailById(EnrollmentCourseId id);
}
