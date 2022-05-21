package com.courses.controller;

import com.courses.controllers.StudentController;
import com.courses.entities.Student;
import com.courses.repositories.StudentRepository;
import com.courses.services.impls.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(StudentServiceImpl.class)
public class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    StudentRepository studentRepository;

    Student student1 = new Student(1L,"Angel", "Felix", "DNI", "12345678", LocalDate.of(1999,5,9), "angel.felix@gmail.com", true);
    Student student2 = new Student(2L,"Jimmy", "Sanchez", "DNI", "87654321", LocalDate.of(1987,1,1), "angel.@gmail.com", true);
    Student student3 = new Student(3L,"Tony", "Sanchez", "DNI", "12312312", LocalDate.of(1996,2,12), "jimmy.sanchez@gmail.com", true);

    @Test
    public void getStudents_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[2].firstName", Matchers.is("Tony")));
    }

    @Test
    public void getStudent_success() throws Exception {

        Mockito.when(studentRepository.findById(eq(1L))).thenReturn(Optional.ofNullable(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is("Angel")));
    }

    @Test
    public void getStudent_by_docNumber_success() throws Exception {

        Mockito.when(studentRepository.findByDocNumber(eq("12345678"))).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/docs/numbers/12345678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is("Angel")));
    }

    @Test
    public void getStudents_by_docType_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findByDocType(eq("DNI"))).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/docs/types/DNI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[2].firstName", Matchers.is("Tony")));
    }

    @Test
    public void saveStudent_success() throws Exception {
        Student newStudent = new Student(0L,"Test", "Test", "DNI", "11111111", LocalDate.of(2020,5,9), "test.test@gmail.com", true);

        Mockito.when(studentRepository.save(eq(newStudent))).thenReturn(newStudent);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is("Test")));
    }

    @Test
    public void editStudent_success() throws Exception {
        Student newStudent = new Student(5L,"Test", "Test", "DNI", "22222222", LocalDate.of(2020,5,9), "test.test@gmail.com", true);

        Mockito.when(studentRepository.existsById(eq(5L))).thenReturn(true);
        Mockito.when(studentRepository.save(eq(newStudent))).thenReturn(newStudent);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/students/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.docNumber", Matchers.is("22222222")));
    }

    @Test
    public void editStudent_fail() throws Exception {
        Student newStudent = new Student(5L,"Test", "Test", "DNI", "22222222", LocalDate.of(2020,5,9), "test.test@gmail.com", true);

        Mockito.when(studentRepository.existsById(eq(5L))).thenReturn(false);
        Mockito.when(studentRepository.save(eq(newStudent))).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/students/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void deleteStudent_success() throws Exception {

        Mockito.when(studentRepository.existsById(eq(5L))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/students/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteStudent_fail() throws Exception {

        Mockito.when(studentRepository.existsById(eq(8L))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/students/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(false)));
    }
}
