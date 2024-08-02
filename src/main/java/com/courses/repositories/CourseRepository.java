package com.courses.repositories;

import com.courses.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatusOrderByUpdatedAtDesc(boolean status);
    List<Course> findByOrderByUpdatedAtDesc();
    Course findByAcronym (String acronym);
    boolean existsByName(String name);
}
