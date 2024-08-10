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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class)
@Import(EnrollmentServiceImpl.class)
class EnrollmentControllerTest {
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

    EnrollmentCourse enrollmentCourse1 = new EnrollmentCourse(new EnrollmentCourseId(1L, 1L));
    EnrollmentCourse enrollmentCourse2 = new EnrollmentCourse(new EnrollmentCourseId(1L, 2L));
    EnrollmentCourse enrollmentCourse3 = new EnrollmentCourse(new EnrollmentCourseId(2L, 3L));

    @Test
    void getEnrollments_success() throws Exception {
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
    void getActivatedEnrollments_success() throws Exception {
        List<Enrollment> enrollments = List.of(enrollment1, enrollment2, enrollment3);

        Mockito.when(enrollmentRepository.findByStatusOrderByEnrollmentDateDesc(true)).thenReturn(enrollments);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments?status=ACTIVATED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[1].studentId", Matchers.is(20)));
    }

    @Test
    void getEnrollments_noContent() throws Exception {
        Mockito.when(enrollmentRepository.findByOrderByEnrollmentDateDesc()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getEnrollment_success() throws Exception {

        Mockito.when(enrollmentRepository.existsById(1L)).thenReturn(true);
        Mockito.when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.studentId", Matchers.is(10)));
    }

    @Test
    void getEnrollment_notPresent() throws Exception {

        Mockito.when(enrollmentRepository.existsById(1L)).thenReturn(true);
        Mockito.when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEnrollment_notFound() throws Exception {

        Mockito.when(enrollmentRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEnrollments_by_date_success() throws Exception {

        List<Enrollment> enrollments = new ArrayList<>(Arrays.asList( enrollment2, enrollment3));
        LocalDate dateInput = LocalDate.of(2022,5,23);

        Mockito.when(enrollmentRepository.findByEnrollmentDateOrderByEnrollmentDateDesc(dateInput)).thenReturn(enrollments);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/dates/2022-05-23")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].studentId", Matchers.is(30)));
    }

    @Test
    void getEnrollments_by_date_noContent() throws Exception {

        LocalDate dateInput = LocalDate.of(2022,5,23);

        Mockito.when(enrollmentRepository.findByEnrollmentDateOrderByEnrollmentDateDesc(dateInput)).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/dates/2022-05-23")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void saveEnrollment_success() throws Exception {
        Enrollment newEnrollment = new Enrollment(0L,40L, LocalDate.of(2022,5,23),true);

        Mockito.when(enrollmentRepository.existsByStudentIdAndStatus(40L, true)).thenReturn(false);
        Mockito.when(enrollmentRepository.save(newEnrollment)).thenReturn(newEnrollment);

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
    void saveEnrollment_notFound() throws Exception {
        Enrollment newEnrollment = new Enrollment(0L,40L, LocalDate.of(2022,5,23),true);

        Mockito.when(enrollmentRepository.existsByStudentIdAndStatus(40L, true)).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newEnrollment));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void editEnrollment_success() throws Exception {
        Enrollment newEnrollment = new Enrollment(5L,10L, LocalDate.of(2022,5,23),true);

        Mockito.when(enrollmentRepository.existsById(5L)).thenReturn(true);
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
    void editEnrollment_fail() throws Exception {
        Enrollment newEnrollment = new Enrollment(5L,50L, LocalDate.of(2022,5,23),true);

        Mockito.when(enrollmentRepository.existsById(5L)).thenReturn(false);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/enrollments/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newEnrollment));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEnrollment_success() throws Exception {
        Enrollment enrollment = new Enrollment(5L,50L, LocalDate.of(2022,5,23),true);
        Enrollment deletedEnrollment = new Enrollment(5L,50L, LocalDate.of(2022,5,23),false);
        Mockito.when(enrollmentRepository.existsById(5L)).thenReturn(true);
        Mockito.when(enrollmentRepository.findById(5L)).thenReturn(Optional.of(enrollment));
        Mockito.when(enrollmentRepository.save(enrollment)).thenReturn(deletedEnrollment);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEnrollment_notFound() throws Exception {
        Mockito.when(enrollmentRepository.existsById(5L)).thenReturn(true);
        Mockito.when(enrollmentRepository.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEnrollment_fail() throws Exception {

        Mockito.when(enrollmentRepository.existsById(8L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEnrollmentDetail_success() throws Exception {

        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);
        Mockito.when(enrollmentCourseRepository.existsById(detailId)).thenReturn(true);
        Mockito.when(enrollmentCourseRepository.findById(detailId)).thenReturn(Optional.of(enrollmentCourse1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id.enrollmentId", Matchers.is(1)))
                .andExpect(jsonPath("$.id.courseId", Matchers.is(1)));
    }

    @Test
    void getEnrollmentDetail_notPresent() throws Exception {

        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);
        Mockito.when(enrollmentCourseRepository.existsById(detailId)).thenReturn(true);
        Mockito.when(enrollmentCourseRepository.findById(detailId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEnrollmentDetail_notFound() throws Exception {

        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);
        Mockito.when(enrollmentCourseRepository.existsById(detailId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveEnrollmentDetail_success() throws Exception {
        EnrollmentCourse detail = new EnrollmentCourse(new EnrollmentCourseId(1L, 1L));

        Mockito.when(enrollmentCourseRepository.existsById(detail.getId())).thenReturn(false);
        Mockito.when(enrollmentCourseRepository.save(detail)).thenReturn(detail);

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
    void saveEnrollmentDetail_noContent() throws Exception {
        EnrollmentCourse detail = new EnrollmentCourse(new EnrollmentCourseId(1L, 1L));

        Mockito.when(enrollmentCourseRepository.existsById(detail.getId())).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments/details")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(detail));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void saveEnrollmentDetails_success() throws Exception {
        List<EnrollmentCourse> details = new ArrayList<>(Arrays.asList( enrollmentCourse2, enrollmentCourse3));
        Mockito.when(enrollmentCourseRepository.existsById(enrollmentCourse2.getId())).thenReturn(false);
        Mockito.when(enrollmentCourseRepository.existsById(enrollmentCourse3.getId())).thenReturn(false);
        Mockito.when(enrollmentCourseRepository.saveAll(details)).thenReturn(details);

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
    void saveEnrollmentDetails_noContent() throws Exception {
        List<EnrollmentCourse> details = new ArrayList<>(Arrays.asList( enrollmentCourse2, enrollmentCourse3));
        Mockito.when(enrollmentCourseRepository.existsById(enrollmentCourse2.getId())).thenReturn(true);
        Mockito.when(enrollmentCourseRepository.existsById(enrollmentCourse3.getId())).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/enrollments/details/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(details));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEnrollmentDetail_success() throws Exception {
        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);

        Mockito.when(enrollmentCourseRepository.existsById(detailId)).thenReturn(true);
        Mockito.doNothing().when(enrollmentCourseRepository).deleteById(detailId);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEnrollmentDetail_fail() throws Exception {
        EnrollmentCourseId detailId = new EnrollmentCourseId(1L, 1L);

        Mockito.when(enrollmentCourseRepository.existsById(detailId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/enrollments/details/1/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
