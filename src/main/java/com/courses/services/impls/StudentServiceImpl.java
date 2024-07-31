package com.courses.services.impls;

import com.courses.entities.Student;
import com.courses.repositories.StudentRepository;
import com.courses.services.CloudinaryService;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CloudinaryService cloudinaryService;

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
        Optional<Student> student = studentRepository.findById(id);
        if(student.isPresent()){
            return studentRepository.findById(id).get();
        } else {
            return null;
        }
    }

    public Student saveStudent(Student student) {
        boolean existsByDocNumber = studentRepository.existsByDocNumber(student.getDocNumber());
        boolean existsByEmail = studentRepository.existsByEmail(student.getEmail());
        if(!existsByDocNumber && !existsByEmail) {
            return studentRepository.save(student);
        }
        return null;
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
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            try {
                if(publicId!= null && !publicId.isEmpty()){
                    cloudinaryService.delete(publicId);
                }

                Map uploadResult = cloudinaryService.upload(image);
                JSONObject json = new JSONObject(uploadResult);
                String url = json.getString("url");
                String publicIdValue = json.getString("public_id");

                student.get().setUrlPhoto(url);
                student.get().setPublicId(publicIdValue);
                studentWithPhoto = studentRepository.save(student.get());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
        return studentWithPhoto;
    }
}
