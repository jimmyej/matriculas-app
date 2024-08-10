package com.courses.repositories;

import com.courses.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatusOrderByUpdatedAtDesc(boolean status);
    List<Course> findByOrderByUpdatedAtDesc();
    Course findByAcronym (String acronym);
    boolean existsByName(String name);
}
