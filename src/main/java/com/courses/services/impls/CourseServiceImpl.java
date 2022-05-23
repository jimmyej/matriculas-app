package com.courses.services.impls;

import com.courses.entities.Course;
import com.courses.repositories.CourseRepository;
import com.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course editCourse(Long id, Course course) {
        boolean exists = courseRepository.existsById(id);
        if(exists){
            return courseRepository.save(course);
        }  else {
            return null;
        }
    }

    @Override
    public boolean deleteCourse(Long id) {
        boolean deleted = false;
        if(courseRepository.existsById(id)){
            courseRepository.deleteById(id);
            deleted = true;
        }
        return deleted;
    }

    @Override
    public Course getCourseByAcronym(String acronym) {
        return courseRepository.findByAcronym(acronym);
    }




}
