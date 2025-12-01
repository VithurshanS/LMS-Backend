package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.lms.Dto.StudentDetailDto;
import org.lms.Dto.UserDetailDto;
import org.lms.Model.Department;
import org.lms.Model.Enrollment;
import org.lms.Model.Student;
import org.lms.Repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StudentService {
    @Inject
    StudentRepository studRepo;

    @Inject
    UserService userService;

    public void createStudent(UUID userId, Department dept){
        studRepo.persist(new Student(userId,dept));
    }

    public List<Enrollment> getEnrollments(UUID studentId){
        return studRepo.findById(studentId).getStudentEnrollments();
    }

    public StudentDetailDto getStudentDetails(UUID studentId){
        StudentDetailDto dto = new StudentDetailDto();
        Student student = studRepo.findById(studentId);
        UserDetailDto userDetail = userService.fetchUserDetail(student.getUserId());
        dto.department = student.getDepartment();
        dto.studentId = student.getId();
        dto.studentUserDetail = userDetail;
        return dto;
    }

    public List<StudentDetailDto> getAllStudents() {
        List<Student> students = studRepo.listAll();
        List<StudentDetailDto> output = new ArrayList<>();
        for (Student i : students) {
            StudentDetailDto dto = new StudentDetailDto();
            dto.studentId = i.getId();
            dto.department = i.getDepartment();
            try {
                dto.studentUserDetail = userService.fetchUserDetail(i.getUserId());
            } catch (Exception e) {
                System.out.println("User not found in Keycloak: " + i.getUserId());
                dto.studentUserDetail = null;
            }
            output.add(dto);
        }
        return output;
    }
}
