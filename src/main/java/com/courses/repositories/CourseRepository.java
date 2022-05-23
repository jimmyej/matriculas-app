package com.courses.repositories;

import com.courses.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByAcronym (String acronym);
}
