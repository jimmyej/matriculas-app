package com.courses.services;

import com.courses.entities.Student;
import com.courses.repositories.StudentRepository;
import com.courses.services.impls.StudentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    Student student1 = new Student(1L,"Angel", "Felix", "DNI", "12345678", LocalDate.of(1999, Calendar.JUNE,9), "angel.felix@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "" );
    Student student2 = new Student(2L,"Jimmy", "Sanchez", "DNI", "87654321", LocalDate.of(1987, Calendar.FEBRUARY,1), "jimmy.sanchez.@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");
    Student student3 = new Student(3L,"Tony", "Sanchez", "DNI", "12312312", LocalDate.of(1996, Calendar.MARCH,12), "tony.sanchez@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");

    @BeforeEach
    public void setup(){
        student = Student.builder()
                .id(1L)
                .firstName("FirstName Test")
                .lastName("LastName Test")
                .docType("DNI")
                .docNumber("12345678")
                .birthDate(LocalDate.of(2006,2,12))
                .email("test.test@gmail.com")
                .status(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .publicId("")
                .urlPhoto("")
                .build();
    }

    @Test
    public void getAllStudentsPaginated_success(){
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));
        Mockito.when(studentRepository.findAll(any(), Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(students));

        Page<Student> result = studentService.getAllStudents("", "", 1, 10);
        Assertions.assertNotNull(result);
    }
}
