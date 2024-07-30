package com.courses.services.impls;

import com.cloudinary.utils.ObjectUtils;
import com.courses.configs.MediaConfig;
import com.courses.entities.Student;
import com.courses.repositories.StudentRepository;
import com.courses.services.StudentService;
import com.courses.services.specs.CommonSpecification;
import com.courses.services.specs.SpecificationHelper;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MediaConfig mediaConfig;

    public Page<Student> getAllStudents(String filters, String sorts, Integer page, Integer size) {
        CommonSpecification<Student> specifications = SpecificationHelper.makeSpecifications(filters);

        List<Order> orderList = SpecificationHelper.makeSorts(sorts);
        Sort sort = Sort.by(orderList);
        Pageable pageable = PageRequest.of(page, size, sort);

        return studentRepository.findAll(specifications, pageable);
    }

    public List<Student> getStudents() {
        return studentRepository.findByOrderByUpdatedAtDesc();
    }

    public List<Student> getActiveStudents() {
        return studentRepository.findByStatus(true);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student editStudent(Long id, Student student) {
        boolean exists = studentRepository.existsById(id);
        if(exists){
            return studentRepository.save(student);
        }  else {
            return null;
        }
    }

    public boolean deleteStudent(Long id) {
        boolean deleted = false;
        if(studentRepository.existsById(id)){
            Student student = studentRepository.findById(id).get();
            student.setStatus(false);
            studentRepository.save(student);
            deleted = true;
        }
        return deleted;
    }

    public Student getStudentByDocNumber(String docNumber) {
        return studentRepository.findByDocNumber(docNumber);
    }

    public List<Student> getStudentsByDocType(String docType) {
        return studentRepository.findByDocType(docType);
    }

    public Student uploadPhoto(Long id, MultipartFile image, String publicId){
        Student studentWithPhoto = null;
        Student student = studentRepository.findById(id).get();
        if (student != null) {
            try {
                File file = Files.createTempFile("temp", image.getOriginalFilename()).toFile();
                image.transferTo(file);

                if(publicId!= null && !publicId.isEmpty()){
                    mediaConfig.cloudinaryConfig().uploader().destroy(publicId, ObjectUtils.emptyMap());
                }

                Map uploadResult = mediaConfig.cloudinaryConfig().uploader().upload(file, ObjectUtils.emptyMap());
                JSONObject json = new JSONObject(uploadResult);
                String url = json.getString("url");
                String publicIdValue = json.getString("public_id");
                file.delete();

                student.setUrlPhoto(url);
                student.setPublicId(publicIdValue);
                studentWithPhoto = studentRepository.save(student);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
        return studentWithPhoto;
    }
}
