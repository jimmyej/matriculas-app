package com.courses.repositories;

import com.courses.entities.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Student findByDocNumber(String docNumber);
    List<Student> findByDocType(String docType);
    List<Student> findByStatusOrderByUpdatedAtDesc(boolean status);
    List<Student> findByOrderByUpdatedAtDesc();
    boolean existsByDocNumber(String docNumber);
    boolean existsByEmail(String email);
}
