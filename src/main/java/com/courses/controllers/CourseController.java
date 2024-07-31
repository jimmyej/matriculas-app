package com.courses.controllers;

import com.courses.entities.Course;
import com.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/courses")
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping(value = "")
    List<Course> getCourses(){
        return courseService.getCourses();
    }

    @GetMapping(value = "/{id}")
    Course getCourseById(@PathVariable Long id){
        return courseService.getCourseById(id);
    }

    @PostMapping(value = "")
    Course saveCourse(@RequestBody Course course){
        return courseService.saveCourse(course);
    }

    @PutMapping(value = "/{id}")
    Course editCourse(@PathVariable Long id, @RequestBody Course course){
        return courseService.editCourse(id, course);
    }

    @DeleteMapping(value = "/{id}")
    boolean deleteCourse(@PathVariable Long id){
        return courseService.deleteCourse(id);
    }

    @GetMapping(value = "/acronym/{acronym}")
    Course getCourseByAcronym(@PathVariable String acronym){
        return courseService.getCourseByAcronym(acronym);
    }


}
