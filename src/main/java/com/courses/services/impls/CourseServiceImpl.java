package com.courses.services.impls;

import com.courses.entities.Course;
import com.courses.repositories.CourseRepository;
import com.courses.services.CourseService;
import com.courses.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

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
            return courseRepository.findById(id).get();
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
            Course course = courseRepository.findById(id).get();
            course.setStatus(false);
            courseRepository.save(course);
            deleted = true;
        }
        return deleted;
    }

    public Course getCourseByAcronym(String acronym) {
        return courseRepository.findByAcronym(acronym);
    }
}
