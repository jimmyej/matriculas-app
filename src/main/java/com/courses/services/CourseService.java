package com.courses.services;

import com.courses.entities.Course;


import java.util.List;

public interface CourseService {
    List<Course> getCourses(String status);
    Course getCourseById(Long id);
    Course saveCourse(Course course);
    Course editCourse(Long id, Course course);
    boolean deleteCourse(Long id);

    Course getCourseByAcronym(String acronym);

}
