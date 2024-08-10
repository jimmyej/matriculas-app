package com.courses.repositories;

import com.courses.entities.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Student findByDocNumber(String docNumber);
    List<Student> findByDocType(String docType);
    List<Student> findByStatusOrderByUpdatedAtDesc(boolean status);
    List<Student> findByOrderByUpdatedAtDesc();
    boolean existsByDocNumber(String docNumber);
    boolean existsByEmail(String email);

    boolean existsById(Long studentId);
    Optional<Student> findById(Long studentId);
    Student save(Student student);

}
