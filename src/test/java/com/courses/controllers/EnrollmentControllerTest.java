package com.courses.controllers;

import com.courses.entities.Enrollment;
import com.courses.entities.EnrollmentCourse;
import com.courses.entities.ids.EnrollmentCourseId;
import com.courses.repositories.EnrollmentCourseRepository;
import com.courses.repositories.EnrollmentRepository;
import com.courses.services.impls.EnrollmentServiceImpl;
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
import java.util.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class)
@Import(EnrollmentServiceImpl.class)
public class EnrollmentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    EnrollmentRepository enrollmentRepository;

    @MockBean
    EnrollmentCourseRepository enrollmentCourseRepository;

    Enrollment enrollment1 = new Enrollment(1L, 10L, LocalDate.of(2022,5,22),true);
    Enrollment enrollment2 = new Enrollment(2L, 20L, LocalDate.of(2022,5,23),true);
    Enrollment enrollment3 = new Enrollment(3L, 30L, LocalDate.of(2022,5,23),true);

    Enrollment enrollment4 = new Enrollment(4L, 30L, LocalDate.of(2022,5,23),false);
    Enrollment enrollment5 = new Enrollment(5L, 30L, LocalDate.of(2022,5,23),false);

    EnrollmentCourse enrollmentCourse1 = new EnrollmentCourse(new EnrollmentCourseId(1L, 1L));
    EnrollmentCourse enrollmentCourse2 = new EnrollmentCourse(new EnrollmentCourseId(1L, 2L));
    EnrollmentCourse enrollmentCourse3 = new EnrollmentCourse(new EnrollmentCourseId(2L, 3L));

    @Test
    public void getEnrollments_success() throws Exception {
        List<Enrollment> enrollments = new ArrayList<>(Arrays.asList(enrollment1, enrollment2, enrollment3));

        Mockito.when(enrollmentRepository.findByOrderByEnrollmentDateDesc()).thenReturn(enrollments);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[1].studentId", Matchers.is(20)));
    }

    @Test
    public void getActivatedEnrollments_success() throws Exception {
        List<Enrollment> enrollments = List.of(enrollment1, enrollment2, enrollment3);

        Mockito.when(enrollmentRepository.findByStatusOrderByEnrollmentDateDesc(eq(true))).thenReturn(enrollments);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments?status=ACTIVATED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[1].studentId", Matchers.is(20)));
    }

    @Test
    public void getEnrollments_noContent() throws Exception {
        Mockito.when(enrollmentRepository.findByOrderByEnrollmentDateDesc()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getEnrollment_success() throws Exception {

        Mockito.when(enrollmentRepository.existsById(eq(1L))).thenReturn(true);
        Mockito.when(enrollmentRepository.findById(eq(1L))).thenReturn(Optional.ofNullable(enrollment1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.studentId", Matchers.is(10)));
    }

    @Test
    public void getEnrollment_notFound() throws Exception {

        Mockito.when(enrollmentRepository.existsById(eq(1L))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getEnrollments_by_date_success() throws Exception {

        List<Enrollment> enrollments = new ArrayList<>(Arrays.asList( enrollment2, enrollment3));
        LocalDate dateInput = LocalDate.of(2022,5,23);

        Mockito.when(enrollmentRepository.findByEnrollmentDateOrderByEnrollmentDateDesc(eq(dateInput))).thenReturn(enrollments);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/dates/2022-05-23")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].studentId", Matchers.is(30)));
    }

    @Test
    public void getEnrollments_by_date_noContent() throws Exception {

        LocalDate dateInput = LocalDate.of(2022,5,23);

        Mockito.when(enrollmentRepository.findByEnrollmentDateOrderByEnrollmentDateDesc(eq(dateInput))).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/dates/2022-05-23")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void saveEnrollment_success() throws Exception {
        Enrollment newEnrollment = new Enrollment(0L,40L, LocalDate.of(2022,5,23),true);

        Mockito.when(enrollmentRepository.existsByStudentIdAndStatus(eq(40L), eq(true))).thenReturn(false);
        Mockito.when(enrollmentRepository.save(eq(newEnrollment))).thenReturn(newEnrollment);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newEnrollment));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.studentId", Matchers.is(40)));
    }

    @Test
    public void saveEnrollment_notFound() throws Exception {
        Enrollment newEnrollment = new Enrollment(0L,40L, LocalDate.of(2022,5,23),true);

        Mockito.when(enrollmentRepository.existsByStudentIdAndStatus(eq(40L), eq(true))).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newEnrollment));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void editEnrollment_success() throws Exception {
        Enrollment newEnrollment = new Enrollment(5L,10L, LocalDate.of(2022,5,23),true);

        Mockito.when(enrollmentRepository.existsById(eq(5L))).thenReturn(true);
        Mockito.when(enrollmentRepository.save(newEnrollment)).thenReturn(newEnrollment);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/enrollments/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newEnrollment));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.studentId", Matchers.is(10)));
    }

    @Test
    public void editEnrollment_fail() throws Exception {
        Enrollment newEnrollment = new Enrollment(5L,50L, LocalDate.of(2022,5,23),true);

        Mockito.when(enrollmentRepository.existsById(eq(5L))).thenReturn(false);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/enrollments/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newEnrollment));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEnrollment_success() throws Exception {
        Enrollment enrollment = new Enrollment(5L,50L, LocalDate.of(2022,5,23),true);
        Enrollment deletedEnrollment = new Enrollment(5L,50L, LocalDate.of(2022,5,23),false);
        Mockito.when(enrollmentRepository.existsById(eq(5L))).thenReturn(true);
        Mockito.when(enrollmentRepository.findById(eq(5L))).thenReturn(Optional.of(enrollment));
        Mockito.when(enrollmentRepository.save(eq(enrollment))).thenReturn(deletedEnrollment);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteEnrollment_fail() throws Exception {

        Mockito.when(enrollmentRepository.existsById(eq(8L))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getEnrollmentDetail_success() throws Exception {

        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);
        Mockito.when(enrollmentCourseRepository.existsById(eq(detailId))).thenReturn(true);
        Mockito.when(enrollmentCourseRepository.findById(eq(detailId))).thenReturn(Optional.ofNullable(enrollmentCourse1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id.enrollmentId", Matchers.is(1)))
                .andExpect(jsonPath("$.id.courseId", Matchers.is(1)));
    }

    @Test
    public void getEnrollmentDetail_notFound() throws Exception {

        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);
        Mockito.when(enrollmentCourseRepository.existsById(eq(detailId))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveEnrollmentDetail_success() throws Exception {
        EnrollmentCourse detail = new EnrollmentCourse(new EnrollmentCourseId(1L, 1L));

        Mockito.when(enrollmentCourseRepository.existsById(eq(detail.getId()))).thenReturn(false);
        Mockito.when(enrollmentCourseRepository.save(eq(detail))).thenReturn(detail);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments/details")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(detail));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.enrollmentId", Matchers.is(1)))
                .andExpect(jsonPath("$.id.courseId", Matchers.is(1)));
    }

    @Test
    public void saveEnrollmentDetail_noContent() throws Exception {
        EnrollmentCourse detail = new EnrollmentCourse(new EnrollmentCourseId(1L, 1L));

        Mockito.when(enrollmentCourseRepository.existsById(eq(detail.getId()))).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments/details")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(detail));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveEnrollmentDetails_success() throws Exception {
        List<EnrollmentCourse> details = new ArrayList<>(Arrays.asList( enrollmentCourse2, enrollmentCourse3));
        Mockito.when(enrollmentCourseRepository.existsById(eq(enrollmentCourse2.getId()))).thenReturn(false);
        Mockito.when(enrollmentCourseRepository.existsById(eq(enrollmentCourse3.getId()))).thenReturn(false);
        Mockito.when(enrollmentCourseRepository.saveAll(eq(details))).thenReturn(details);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments/details/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(details));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id.enrollmentId", Matchers.is(2)))
                .andExpect(jsonPath("$[1].id.courseId", Matchers.is(3)));
    }

    @Test
    public void saveEnrollmentDetails_noContent() throws Exception {
        List<EnrollmentCourse> details = new ArrayList<>(Arrays.asList( enrollmentCourse2, enrollmentCourse3));
        Mockito.when(enrollmentCourseRepository.existsById(eq(enrollmentCourse2.getId()))).thenReturn(true);
        Mockito.when(enrollmentCourseRepository.existsById(eq(enrollmentCourse3.getId()))).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments/details/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(details));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteEnrollmentDetail_success() throws Exception {
        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);

        Mockito.when(enrollmentCourseRepository.existsById(eq(detailId))).thenReturn(true);
        Mockito.doNothing().when(enrollmentCourseRepository).deleteById(eq(detailId));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteEnrollmentDetail_fail() throws Exception {
        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);

        Mockito.when(enrollmentCourseRepository.existsById(eq(detailId))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
