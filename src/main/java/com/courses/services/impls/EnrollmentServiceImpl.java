package com.courses.services.impls;

import com.courses.entities.Enrollment;
import com.courses.entities.EnrollmentCourse;
import com.courses.entities.ids.EnrollmentCourseId;
import com.courses.repositories.EnrollmentCourseRepository;
import com.courses.repositories.EnrollmentRepository;
import com.courses.services.EnrollmentService;
import com.courses.enums.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    EnrollmentCourseRepository enrollmentCourseRepository;

    public List<Enrollment> getEnrollments(String status) {
        if(status != null) {
            boolean activeEnrollments = status.equals(CommonConstants.ACTIVATED.name());
            return enrollmentRepository.findByStatusOrderByEnrollmentDateDesc(activeEnrollments);
        } else {
            return enrollmentRepository.findByOrderByEnrollmentDateDesc();
        }
    }

    public Enrollment getEnrollmentById(Long id) {
        boolean existsById = enrollmentRepository.existsById(id);
        if(existsById){
            return enrollmentRepository.findById(id).get();
        }
        return null;
    }

    public Enrollment saveEnrollment(Enrollment enrollment) {
        boolean existsByStudentIdAndStatus = enrollmentRepository.existsByStudentIdAndStatus(enrollment.getStudentId(), enrollment.isStatus());
        if(!existsByStudentIdAndStatus) {
            return enrollmentRepository.save(enrollment);
        }
        return null;
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
            Enrollment enrollment = enrollmentRepository.findById(id).get();
            enrollment.setStatus(false);
            enrollmentRepository.save(enrollment);
            deleted = true;
        }
        return deleted;
    }

    public List<Enrollment> getEnrollmentsByDate(LocalDate enrollmentDate){
        return enrollmentRepository.findByEnrollmentDateOrderByEnrollmentDateDesc(enrollmentDate);
    }

    public EnrollmentCourse getDetailById(EnrollmentCourseId id) {
        boolean existsById = enrollmentCourseRepository.existsById(id);
        if(existsById){
            return enrollmentCourseRepository.findById(id).get();
        }
        return null;
    }

    public EnrollmentCourse saveDetail(EnrollmentCourse detail) {
        boolean existsById = enrollmentCourseRepository.existsById(detail.getId());
        if(!existsById){
            return enrollmentCourseRepository.save(detail);
        }
        return null;
    }

    public List<EnrollmentCourse> saveDetails(List<EnrollmentCourse> details) {
        List<EnrollmentCourse> filteredDetails = details.stream().filter(d -> !enrollmentCourseRepository.existsById(d.getId())).collect(Collectors.toList());
        if(!filteredDetails.isEmpty()){
            return enrollmentCourseRepository.saveAll(filteredDetails);
        }
        return List.of();
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
