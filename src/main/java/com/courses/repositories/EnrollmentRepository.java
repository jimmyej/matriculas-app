package com.courses.repositories;

import com.courses.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByOrderByEnrollmentDateDesc();
    List<Enrollment> findByStatusOrderByEnrollmentDateDesc(boolean status);
    List<Enrollment> findByEnrollmentDateOrderByEnrollmentDateDesc(LocalDate enrollmentDate);
    boolean existsByStudentIdAndStatus(Long studentId, boolean status);
}
