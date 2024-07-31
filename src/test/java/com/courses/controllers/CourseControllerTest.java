package com.courses.controllers;

import com.courses.entities.Course;
import com.courses.repositories.CourseRepository;
import com.courses.services.impls.CourseServiceImpl;
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

@WebMvcTest(CourseController.class)
@Import(CourseServiceImpl.class)
public class CourseControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CourseRepository courseRepository;

    Course course1 = new Course(1L,"Oracle","ORL",LocalDate.of(2022,8,3),true);
    Course course2 = new Course(2L,"Python","PTN",LocalDate.of(2022,8,3),true);
    Course course3 = new Course(3L,"Java","JDK",LocalDate.of(2022,8,3),true);

    @Test
    public void getCourses_success() throws Exception {
        List<Course> courses = new ArrayList<>(Arrays.asList(course1, course2, course3));

        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    public void getCourse_success() throws Exception {

        Mockito.when(courseRepository.findById(eq(1L))).thenReturn(Optional.ofNullable(course1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Oracle")));
    }

    @Test
    public void getCourse_by_acronym_success() throws Exception {

        Mockito.when(courseRepository.findByAcronym(eq("JDK"))).thenReturn(course3);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses/acronym/JDK")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Java")));
    }


    @Test
    public void saveCourse_success() throws Exception {
        Course newCourse = new Course(0L,"Office","OF",LocalDate.of(2022,8,3),true);

        Mockito.when(courseRepository.save(eq(newCourse))).thenReturn(newCourse);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newCourse));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Office")));
    }

    @Test
    public void editCourse_success() throws Exception {
        Course newCourse = new Course(5L,"Office3","OF",LocalDate.of(2022,8,3),true);

        Mockito.when(courseRepository.existsById(eq(5L))).thenReturn(true);
        Mockito.when(courseRepository.save(eq(newCourse))).thenReturn(newCourse);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newCourse));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Office3")));
    }

    @Test
    public void editCourse_fail() throws Exception {
        Course newCourse = new Course(5L,"Office3","OF",LocalDate.of(2022,8,3),true);

        Mockito.when(courseRepository.existsById(eq(5L))).thenReturn(false);
        Mockito.when(courseRepository.save(eq(newCourse))).thenReturn(null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newCourse));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCourse_success() throws Exception {

        Mockito.when(courseRepository.existsById(eq(5L))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/courses/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCourse_fail() throws Exception {

        Mockito.when(courseRepository.existsById(eq(8L))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/courses/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(false)));
    }
}
