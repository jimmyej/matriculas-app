package com.courses.services.impls;

import com.courses.entities.Enrollment;
import com.courses.entities.EnrollmentCourse;
import com.courses.entities.ids.EnrollmentCourseId;
import com.courses.repositories.EnrollmentCourseRepository;
import com.courses.repositories.EnrollmentRepository;
import com.courses.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    EnrollmentCourseRepository enrollmentCourseRepository;

    public List<Enrollment> getEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id).get();
    }

    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment editEnrollment(Long id, Enrollment enrollment) {
        boolean exists = enrollmentRepository.existsById(id);
        if(exists){
            return enrollmentRepository.save(enrollment);
        }  else {
            return null;
        }
    }

    public boolean deleteEnrollment(Long id) {
        boolean deleted = false;
        if(enrollmentRepository.existsById(id)){
            enrollmentRepository.deleteById(id);
            deleted = true;
        }
        return deleted;
    }

    public List<Enrollment> getEnrollmentsByDate(LocalDate enrollmentDate){
        return enrollmentRepository.findByEnrollmentDate(enrollmentDate);
    }

    public EnrollmentCourse getDetailById(EnrollmentCourseId id) {
        return enrollmentCourseRepository.findById(id).get();
    }

    public EnrollmentCourse saveDetail(EnrollmentCourse detail) {
        return enrollmentCourseRepository.save(detail);
    }

    public List<EnrollmentCourse> saveDetails(List<EnrollmentCourse> details) {
        return enrollmentCourseRepository.saveAll(details);
    }

    public boolean deleteDetailById(EnrollmentCourseId id) {
        boolean deleted = false;
        if(enrollmentCourseRepository.existsById(id)) {
            enrollmentCourseRepository.deleteById(id);
            deleted = true;
        }
        return deleted;
    }
}
