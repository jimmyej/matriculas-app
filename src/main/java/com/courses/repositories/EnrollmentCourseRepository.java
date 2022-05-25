package com.courses.repositories;

import com.courses.entities.EnrollmentCourse;
import com.courses.entities.ids.EnrollmentCourseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentCourseRepository extends JpaRepository<EnrollmentCourse, EnrollmentCourseId> {

}
