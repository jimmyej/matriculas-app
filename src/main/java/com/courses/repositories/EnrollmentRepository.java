package com.courses.repositories;

import com.courses.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByEnrollmentDate(LocalDate enrollmentDate);
}
