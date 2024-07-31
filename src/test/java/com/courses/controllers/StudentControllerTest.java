package com.courses.controllers;

import com.courses.entities.Student;
import com.courses.repositories.StudentRepository;
import com.courses.services.CloudinaryService;
import com.courses.services.impls.StudentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(StudentServiceImpl.class)
class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    CloudinaryService cloudinaryService;

    Student student1 = new Student(1L,"Angel", "Felix", "DNI", "12345678", LocalDate.of(1999, Calendar.JUNE,9), "angel.felix@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "" );
    Student student2 = new Student(2L,"Jimmy", "Sanchez", "DNI", "87654321", LocalDate.of(1987, Calendar.FEBRUARY,1), "jimmy.sanchez.@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");
    Student student3 = new Student(3L,"Tony", "Sanchez", "DNI", "12312312", LocalDate.of(1996, Calendar.MARCH,12), "tony.sanchez@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");

    Student student4 = new Student(3L,"Salvador", "Sanchez", "DNI", "43563412", LocalDate.of(2016, Calendar.FEBRUARY,2), "salvador.sanchez@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "http://clouddinary.com/123456789.png", "123456789");
    Student student5 = new Student(5L,"Test", "Test", "DNI", "56873456", LocalDate.of(2016, Calendar.FEBRUARY,2), "salvador.sanchez@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "http://clouddinary.com/123456789.png", "");


    private static Student getStudent() {
        Student newStudent = new Student();
        newStudent.setFirstName("Angel");
        newStudent.setLastName("Felix");
        newStudent.setDocType("DNI");
        newStudent.setDocNumber("12345678");
        newStudent.setBirthDate(LocalDate.of(1999, Calendar.JUNE,9));
        newStudent.setEmail("angel.felix@gmail.com");
        newStudent.setStatus(true);
        newStudent.setCreatedAt(LocalDateTime.now());
        newStudent.setUpdatedAt(LocalDateTime.now());
        newStudent.setUrlPhoto("");
        newStudent.setPublicId("");
        return newStudent;
    }

    @Test
    public void getAllStudents_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findByOrderByUpdatedAtDesc()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students?showAll=true")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    public void getAllStudents_noContent() throws Exception {
        Mockito.when(studentRepository.findByOrderByUpdatedAtDesc()).thenReturn(Lists.list());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students?showAll=true")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void getActivatedStudents_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findByStatus(anyBoolean())).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students?showAll=false")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    public void getStudentsPaginated_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findAll(any(), Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(students));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/1/10")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.totalElements", Matchers.is(3)))
                .andExpect(jsonPath("$.numberOfElements", Matchers.is(3)))
                .andExpect(jsonPath("$.totalPages", Matchers.is(1)))
                .andExpect(jsonPath("$.content", Matchers.hasSize(3)));
    }

    @Test
    public void getStudentsPaginated_noContent() throws Exception {
        Mockito.when(studentRepository.findAll(any(), Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/1/10")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
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
    public void getStudent_notFound() throws Exception {

        Mockito.when(studentRepository.findById(eq(1L))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
    public void getStudent_by_docNumber_notFound() throws Exception {

        Mockito.when(studentRepository.findByDocNumber(eq("12345678"))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/docs/numbers/12345678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
    public void getStudents_by_docType_noContent() throws Exception {
        Mockito.when(studentRepository.findByDocType(eq("DNI"))).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/docs/types/DNI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void saveStudent_success() throws Exception {
        Student newStudent = getStudent();

        Mockito.when(studentRepository.save(any())).thenReturn(student1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is("Angel")));
    }

    @Test
    public void saveStudent_notFound() throws Exception {
        Student newStudent = getStudent();

        Mockito.when(studentRepository.existsByDocNumber(any())).thenReturn(false);
        Mockito.when(studentRepository.existsByEmail(any())).thenReturn(false);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveStudent_ExistingDocNumberAndEmail() throws Exception {
        Student newStudent = getStudent();

        Mockito.when(studentRepository.existsByDocNumber(any())).thenReturn(true);
        Mockito.when(studentRepository.existsByEmail(any())).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveStudent_ExistingDocNumber() throws Exception {
        Student newStudent = getStudent();

        Mockito.when(studentRepository.existsByDocNumber(any())).thenReturn(true);
        Mockito.when(studentRepository.existsByEmail(any())).thenReturn(false);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveStudent_ExistingEmail() throws Exception {
        Student newStudent = getStudent();

        Mockito.when(studentRepository.existsByDocNumber(any())).thenReturn(false);
        Mockito.when(studentRepository.existsByEmail(any())).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void editStudent_success() throws Exception {
        Student editedStudent = new Student(5L,"Test", "Test", "DNI", "22222222", LocalDate.of(2020, Calendar.JUNE,9), "test.test@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");

        Mockito.when(studentRepository.existsById(5L)).thenReturn(true);
        Mockito.when(studentRepository.save(any())).thenReturn(editedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/students/5")
                        .content(mapper.writeValueAsString(editedStudent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.docNumber", Matchers.is("22222222")));
    }

    @Test
    public void editStudent_notFound() throws Exception {
        Student editedStudent = new Student(5L,"Test", "Test", "DNI", "22222222", LocalDate.of(2020, Calendar.JUNE,9), "test.test@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");

        Mockito.when(studentRepository.existsById(5L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/students/5")
                        .content(mapper.writeValueAsString(editedStudent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editStudent_fail() throws Exception {
        Student newStudent = new Student(5L,"Test", "Test", "DNI", "22222222", LocalDate.of(2020, Calendar.JUNE,9), "test.test@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");

        Mockito.when(studentRepository.existsById(eq(5L))).thenReturn(false);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/students/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteStudent_success() throws Exception {

        Mockito.when(studentRepository.existsById(eq(3L))).thenReturn(true);
        Mockito.when(studentRepository.findById(eq(3L))).thenReturn(Optional.of(student3));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/students/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteStudent_fail() throws Exception {

        Mockito.when(studentRepository.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/students/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void uploadStudentPhotoNewPublicId_success() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("url", "http://clouddinary.com/123456789.png");
        resultMap.put("public_id", "123456789");

        Mockito.when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student5));
        Mockito.when(cloudinaryService.upload(firstFile)).thenReturn(resultMap);
        Mockito.when(studentRepository.save(any())).thenReturn(student5);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/v1/students/5/upload")
                .file(firstFile);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void uploadStudentExistingPhoto_success() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());
        Map<String, String> mapImage = new HashMap<>();
        mapImage.put("url", "http://clouddinary.com/123456789.png");
        mapImage.put("public_id", "123456789");

        Mockito.when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student4));
        Mockito.when(cloudinaryService.delete("123456789")).thenReturn(Map.of());
        Mockito.when(cloudinaryService.upload(firstFile)).thenReturn(mapImage);
        Mockito.when(studentRepository.save(any())).thenReturn(student4);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/v1/students/4/upload/123456789")
                .file(firstFile);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void uploadStudentPhoto_notFound() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());

        Mockito.when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/v1/students/4/upload")
                .file(firstFile);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void uploadStudentPhoto_nullPublicId() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("url", "http://clouddinary.com/123456789.png");
        resultMap.put("public_id", "123456789");

        Mockito.when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student5));
        Mockito.when(cloudinaryService.upload(firstFile)).thenReturn(resultMap);
        Mockito.when(studentRepository.save(any())).thenReturn(student5);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/v1/students/5/upload/")
                .file(firstFile);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void uploadStudentPhotoNewPublicId_throwsException() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());;

        Mockito.when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student5));
        Mockito.when(cloudinaryService.upload(firstFile)).thenThrow(new IOException());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/v1/students/5/upload")
                .file(firstFile);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }
}
