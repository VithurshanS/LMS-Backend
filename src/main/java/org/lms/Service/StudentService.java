package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.lms.Dto.StudentDetailDto;
import org.lms.Dto.UserDetailDto;
import org.lms.Dto.UserResponseDto;
import org.lms.Model.Department;
import org.lms.Model.Enrollment;
import org.lms.Model.Student;
import org.lms.Repository.EnrollmentRepository;
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

    @Inject
    EnrollmentRepository enrollmentRepository;

    public void createStudent(UUID userId, Department dept){
        Student student = new Student(userId, dept);
        studRepo.persist(student);
    }

    public List<Enrollment> getEnrollments(UUID studentId){
        return studRepo.findById(studentId).getStudentEnrollments();
    }

    public UserResponseDto getStudentDetails(UUID userId){
        UserResponseDto user = new UserResponseDto();
        Student lect = studRepo.findByUserId(userId);
        user.departmentId = (lect.getDepartment() != null)
                ? lect.getDepartment().getId()
                : null;
        user.id = lect.getId();
        UserDetailDto userDetail;
        try {
            userDetail = userService.fetchUserDetail(lect.getUserId());
        } catch (Exception e) {
            System.out.println("User not found in Keycloak: " + lect.getUserId());
            return user;

        }
        user.username = userDetail.userName;
        user.email = userDetail.email;
        user.firstName = userDetail.firstName;
        user.lastName = userDetail.lastName;
        user.role = userDetail.clientRole.get(0).toUpperCase();
        user.isActive = userDetail.isActive;
        return user;


    }

    public List<StudentDetailDto> getAllStudents() {
        List<Student> students = studRepo.listAll();
        List<StudentDetailDto> output = new ArrayList<>();
        for (Student i : students) {
            StudentDetailDto dto = new StudentDetailDto();
            dto.studentId = i.getId();
            dto.departmentId = (i.getDepartment() != null)
                    ? i.getDepartment().getId()
                    : null;
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

    public UserResponseDto getStudentDetails2(UUID studentId){
        UserResponseDto user = new UserResponseDto();
        Student lect = studRepo.findById(studentId);
        user.departmentId = (lect.getDepartment() != null)
                ? lect.getDepartment().getId()
                : null;
        user.id = lect.getId();
        UserDetailDto userDetail;
        try {
            userDetail = userService.fetchUserDetail(lect.getUserId());
        } catch (Exception e) {
            System.out.println("User not found in Keycloak: " + lect.getUserId());
            return user;

        }
        user.username = userDetail.userName;
        user.email = userDetail.email;
        user.firstName = userDetail.firstName;
        user.lastName = userDetail.lastName;
        user.role = userDetail.clientRole.get(0).toUpperCase();
        user.isActive = userDetail.isActive;
        return user;


    }
    public List<UserResponseDto> getStudentDetailsbyDepartmentId(UUID departmentId) {

        List<Student> lecturers = studRepo.findByDepartmentId(departmentId);

        List<UserResponseDto> output = new ArrayList<>();
        for (Student lecturer : lecturers) {
            UserResponseDto dto = getStudentDetails2(lecturer.getId());
            output.add(dto);
        }
        return output;
    }
    public List<UserResponseDto> getStudentsByModuleId(UUID moduleId){
        List<Student> students = enrollmentRepository.findStudentsByModuleId(moduleId);
        return students.stream()
                .map(m -> getStudentDetails2(m.getId()))
                .toList();
    }



    public List<UserResponseDto> getAllStudents2(){
        List<Student> lecturers = studRepo.listAll();
        List<UserResponseDto> output = new ArrayList<>();
        for (Student i : lecturers) {
            UserResponseDto dto = new UserResponseDto();
            dto = getStudentDetails2(i.getId());
            output.add(dto);
        }
        return output;
    }
}
