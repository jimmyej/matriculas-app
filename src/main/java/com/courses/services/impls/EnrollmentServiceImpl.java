package com.courses.services.impls;

import com.courses.entities.Enrollment;
import com.courses.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private com.courses.repositories.EnrollmentRepository enrollmentRepository;

    @Override
    public List<Enrollment> getEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id).get();
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment editEnrollment(Long id, Enrollment enrollment) {
        boolean exists = enrollmentRepository.existsById(id);
        if(exists){
            return enrollmentRepository.save(enrollment);
        }  else {
            return null;
        }
    }

    @Override
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




}
