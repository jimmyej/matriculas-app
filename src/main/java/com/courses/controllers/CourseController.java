package com.courses.controllers;

import com.courses.entities.Course;
import com.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/v1/courses")
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping(value = "")
    ResponseEntity<List<Course>> getCourses(@RequestParam(required=false) String status){
        List<Course> courses = courseService.getCourses(status);
        if(courses.isEmpty()){
            return new ResponseEntity<>(courses, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Course> getCourseById(@PathVariable Long id){
        Course course = courseService.getCourseById(id);
        if(course == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PostMapping(value = "")
    ResponseEntity<Course> saveCourse(@RequestBody Course course){
        Course newCourse = courseService.saveCourse(course);
        if(newCourse == null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        return new ResponseEntity<>(newCourse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<Course> editCourse(@PathVariable Long id, @RequestBody Course course){
        Course newCourse =  courseService.editCourse(id, course);
        if(newCourse == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newCourse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Boolean> deleteCourse(@PathVariable Long id){
        boolean isDeleted = courseService.deleteCourse(id);
        if(!isDeleted){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/acronym/{acronym}")
    ResponseEntity<Course> getCourseByAcronym(@PathVariable String acronym){
        Course course = courseService.getCourseByAcronym(acronym);
        if(course == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}
