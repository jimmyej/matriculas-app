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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
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

    Course course1 = new Course(1L,"Oracle","ORL",LocalDate.of(2022,8,3), LocalDateTime.now(),true);
    Course course2 = new Course(2L,"Python","PTN",LocalDate.of(2022,8,3),LocalDateTime.now(),true);
    Course course3 = new Course(3L,"Java","JDK",LocalDate.of(2022,8,3),LocalDateTime.now(),true);

    Course course4 = new Course(4L,"Javascript","JS",LocalDate.of(2022,8,3),LocalDateTime.now(),false);

    private static Course getCourse(){
        Course course = new Course();
        course.setName("Office");
        course.setAcronym("OF");
        course.setDuration(LocalDate.of(2022,8,3));
        course.setUpdatedAt(LocalDateTime.now());
        course.setStatus(true);
        return course;
    }

    @Test
    public void getCourses_notFound() throws Exception {
        Mockito.when(courseRepository.findByOrderByUpdatedAtDesc()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getCourses_success() throws Exception {
        List<Course> courses = new ArrayList<>(Arrays.asList(course1, course2, course3));

        Mockito.when(courseRepository.findByOrderByUpdatedAtDesc()).thenReturn(courses);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    public void getActivatedCourses_success() throws Exception {
        Mockito.when(courseRepository.findByStatusOrderByUpdatedAtDesc(true)).thenReturn(List.of(course1, course2, course3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses?status=ACTIVATED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getInactivatedCourses_success() throws Exception {
        Mockito.when(courseRepository.findByStatusOrderByUpdatedAtDesc(false)).thenReturn(List.of(course4));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses?status=INACTIVATED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getCourseById_success() throws Exception {
        Mockito.when(courseRepository.existsById(eq(1L))).thenReturn(true);
        Mockito.when(courseRepository.findById(eq(1L))).thenReturn(Optional.of(course1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Oracle")));
    }

    @Test
    public void getCourseById_notFound() throws Exception {
        Mockito.when(courseRepository.existsById(eq(1L))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
    public void getCourse_by_acronym_notFound() throws Exception {
        Mockito.when(courseRepository.findByAcronym(eq("JDK"))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/courses/acronym/JDK")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveCourse_success() throws Exception {
        Course newCourse = getCourse();

        Mockito.when(courseRepository.existsByName(eq(newCourse.getName()))).thenReturn(false);
        Mockito.when(courseRepository.save(any())).thenReturn(newCourse);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newCourse));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", Matchers.is("Office")));
    }

    @Test
    public void saveCourse_foundExistingCourse() throws Exception {
        Course newCourse = getCourse();

        Mockito.when(courseRepository.existsByName(eq(newCourse.getName()))).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newCourse));

        mockMvc.perform(mockRequest)
                .andExpect(status().isFound());
    }

    @Test
    public void editCourse_success() throws Exception {
        Course newCourse = new Course(5L,"Office3","OF",LocalDate.of(2022,8,3),LocalDateTime.now(),true);

        Mockito.when(courseRepository.existsById(eq(5L))).thenReturn(true);
        Mockito.when(courseRepository.save(any())).thenReturn(newCourse);

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
        Course newCourse = new Course(5L,"Office3","OF",LocalDate.of(2022,8,3),LocalDateTime.now(),true);

        Mockito.when(courseRepository.existsById(5L)).thenReturn(false);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/courses/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newCourse));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCourse_success() throws Exception {
        Course course = new Course(5L,"Office3","OF",LocalDate.of(2022,8,3),LocalDateTime.now(),true);
        Mockito.when(courseRepository.existsById(eq(5L))).thenReturn(true);
        Mockito.when(courseRepository.findById(eq(5L))).thenReturn(Optional.of(course));
        Mockito.when(courseRepository.findById(any())).thenReturn(Optional.of(course));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/courses/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCourse_fail() throws Exception {
        Mockito.when(courseRepository.existsById(eq(8L))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/courses/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
