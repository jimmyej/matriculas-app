package com.courses.services.impls;

import com.courses.entities.Course;
import com.courses.repositories.CourseRepository;
import com.courses.services.CourseService;
import com.courses.enums.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;

    @Autowired
    CourseServiceImpl(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public List<Course> getCourses(String status) {
        if(status != null) {
            boolean activeStudents = status.equals(CommonConstants.ACTIVATED.name());
            return courseRepository.findByStatusOrderByUpdatedAtDesc(activeStudents);
        } else {
            return courseRepository.findByOrderByUpdatedAtDesc();
        }
    }

    public Course getCourseById(Long id) {
        boolean exists = courseRepository.existsById(id);
        if(exists){
            Optional<Course> course = courseRepository.findById(id);
            if(course.isPresent()){
                return course.get();
            }
        }
        return null;
    }

    public Course saveCourse(Course course) {
        boolean existsByName = courseRepository.existsByName(course.getName());
        if(!existsByName){
            return courseRepository.save(course);
        }
        return null;
    }

    public Course editCourse(Long id, Course course) {
        boolean exists = courseRepository.existsById(id);
        if(exists){
            return courseRepository.save(course);
        }  else {
            return null;
        }
    }

    public boolean deleteCourse(Long id) {
        boolean deleted = false;
        if(courseRepository.existsById(id)){
            Optional<Course> course = courseRepository.findById(id);
            if(course.isPresent()){
                course.get().setStatus(false);
                courseRepository.save(course.get());
                deleted = true;
            }
        }
        return deleted;
    }

    public Course getCourseByAcronym(String acronym) {
        return courseRepository.findByAcronym(acronym);
    }
}
