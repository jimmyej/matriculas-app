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
    void getAllStudents_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findByOrderByUpdatedAtDesc()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    void getAllStudents_noContent() throws Exception {
        Mockito.when(studentRepository.findByOrderByUpdatedAtDesc()).thenReturn(Lists.list());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void getActivatedStudents_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findByStatusOrderByUpdatedAtDesc(anyBoolean())).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students?status=ACTIVATED")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }

    @Test
    void getStudentsPaginated_success() throws Exception {
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
    void getStudentsPaginated_noContent() throws Exception {
        Mockito.when(studentRepository.findAll(any(), Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/1/10")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void getStudent_success() throws Exception {
        Mockito.when(studentRepository.existsById(1L)).thenReturn(true);
        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is("Angel")));
    }

    @Test
    void getStudent_notPresent() throws Exception {
        Mockito.when(studentRepository.existsById(1L)).thenReturn(true);
        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudent_notFound() throws Exception {
        Mockito.when(studentRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudent_by_docNumber_success() throws Exception {

        Mockito.when(studentRepository.findByDocNumber("12345678")).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/docs/numbers/12345678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is("Angel")));
    }

    @Test
    void getStudent_by_docNumber_notFound() throws Exception {

        Mockito.when(studentRepository.findByDocNumber("12345678")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/docs/numbers/12345678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudents_by_docType_success() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2, student3));

        Mockito.when(studentRepository.findByDocType("DNI")).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/docs/types/DNI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[2].firstName", Matchers.is("Tony")));
    }

    @Test
    void getStudents_by_docType_noContent() throws Exception {
        Mockito.when(studentRepository.findByDocType("DNI")).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/students/docs/types/DNI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void saveStudent_success() throws Exception {
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

    void saveStudent_notFound(boolean existsDocNumber, boolean existsEmail) throws Exception {
        Student newStudent = getStudent();

        Mockito.when(studentRepository.existsByDocNumber(any())).thenReturn(existsDocNumber);
        Mockito.when(studentRepository.existsByEmail(any())).thenReturn(existsEmail);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void saveStudent_notFound_existsDocNumberAndExistsEmail() throws Exception {
        saveStudent_notFound(true,true);
    }

    @Test
    void saveStudent_notFound_existsDocNumberButNotExistsEmail() throws Exception {
        saveStudent_notFound(true,false);
    }

    @Test
    void saveStudent_notFound_notExistsDocNumberButExistsEmail() throws Exception {
        saveStudent_notFound(false,true);
    }

    @Test
    void editStudent_success() throws Exception {
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
    void editStudent_notFound() throws Exception {
        Student editedStudent = new Student(5L,"Test", "Test", "DNI", "22222222", LocalDate.of(2020, Calendar.JUNE,9), "test.test@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");

        Mockito.when(studentRepository.existsById(5L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/students/5")
                        .content(mapper.writeValueAsString(editedStudent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void editStudent_fail() throws Exception {
        Student newStudent = new Student(5L,"Test", "Test", "DNI", "22222222", LocalDate.of(2020, Calendar.JUNE,9), "test.test@gmail.com", true, LocalDateTime.now(), LocalDateTime.now(), "", "");

        Mockito.when(studentRepository.existsById(5L)).thenReturn(false);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/v1/students/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudent_success() throws Exception {

        student3.setStatus(false);
        Mockito.when(studentRepository.existsById(3L)).thenReturn(true);
        Mockito.when(studentRepository.findById(3L)).thenReturn(Optional.of(student3));
        Mockito.when(studentRepository.save(any())).thenReturn(student3);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/students/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudent_notPresent() throws Exception {

        student3.setStatus(false);
        Mockito.when(studentRepository.existsById(3L)).thenReturn(true);
        Mockito.when(studentRepository.findById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/students/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudent_fail() throws Exception {

        Mockito.when(studentRepository.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/students/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    void uploadStudentPhoto(Student student, boolean existsPhoto) throws Exception {
        String sufixPath = "", publicId = "123456789";
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("url", "http://clouddinary.com/123456789.png");
        resultMap.put("public_id", publicId);

        Mockito.when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        if(existsPhoto){
            Mockito.doNothing().when(cloudinaryService).delete(publicId);
            sufixPath = "/"+publicId;
        }
        Mockito.when(cloudinaryService.upload(firstFile)).thenReturn(resultMap);
        Mockito.when(studentRepository.save(any())).thenReturn(student);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/v1/students/"+student.getId().intValue()+"/upload"+sufixPath)
                .file(firstFile);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void uploadStudentPhotoNewPublicId_success() throws Exception {
        uploadStudentPhoto(student5, false);
    }

    @Test
    void uploadStudentExistingPhoto_success() throws Exception {
        uploadStudentPhoto(student4, true);
    }

    @Test
    void uploadStudentPhoto_notFound() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());

        Mockito.when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/v1/students/4/upload")
                .file(firstFile);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadStudentPhoto_nullPublicId() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());
        Map<String, Object> resultMap = new HashMap<>();
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
    void uploadStudentPhotoNewPublicId_throwsException() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes());

        Mockito.when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student5));
        Mockito.when(cloudinaryService.upload(firstFile)).thenThrow(new IOException());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/v1/students/5/upload")
                .file(firstFile);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }
}
