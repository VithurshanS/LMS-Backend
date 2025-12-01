package org.lms.EntityRelationshipTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.lms.Model.Department;
import org.lms.Model.Enrollment;
import org.lms.Model.Lecturer;
import org.lms.Model.Student;
import org.lms.Model.UserDB;
import org.lms.Repository.AdminRepository;
import org.lms.Repository.DepartmentRepository;
import org.lms.Repository.EnrollmentRepository;
import org.lms.Repository.LecturerRepository;
import org.lms.Repository.ModuleRepository;
import org.lms.Repository.StudentRepository;
import org.lms.Repository.UserRepository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentTest {
    
    @Inject
    LecturerRepository lecturerRepository;

    @Inject
    StudentRepository studentRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    ModuleRepository moduleRepository;

    @Inject
    AdminRepository adminRepository;

    @Inject
    EnrollmentRepository enrollmentRepository;



    @Test
    @Transactional
    public void createStudentTestvalid(){
        Student student = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student1", "Computer Science");
        assertNotNull(student);
        assertEquals("student1", student.getUserId());
        assertNotNull(student.getDepartment());
        assertEquals("Computer Science", student.getDepartment().getName());
    }

    @Test
    @Transactional
    public void createStudentWithWrongRole(){
        Department foundCsDept = TestHelper.createDepartment(departmentRepository, "Computer Science");
        
        Student student = new Student("lecturer123", foundCsDept);
        studentRepository.persist(student);
        assertNotNull(student);
        assertEquals("lecturer123", student.getUserId());
    }

    @Test
    @Transactional
    public void createLecturerWithWrongRole2(){
        Department foundCsDept = TestHelper.createDepartment(departmentRepository, "Computer Science");
        
        Student student = new Student("admin123", foundCsDept);
        studentRepository.persist(student);
        assertNotNull(student);
        assertEquals("admin123", student.getUserId());
    }

    @Test
    @Transactional
    public void listAllEnrollmentsOfStudent(){
        Lecturer lecturer1 = TestHelper.createLecturer(lecturerRepository, userRepository,departmentRepository, "lecturer2","Mathematics");
        org.lms.Model.Module module1 = TestHelper.createModule(moduleRepository, lecturerRepository, departmentRepository, userRepository, adminRepository, "CS100", "Data Structure", 50, "lecturer2", "Mathematics", "admin123");
        org.lms.Model.Module module2 = TestHelper.createModule(moduleRepository, lecturerRepository, departmentRepository, userRepository, adminRepository, "CS101", "Linear Algebra", 50, "lecturer2", "Mathematics", "admin123");
        Student student = TestHelper.createStudent(studentRepository,userRepository,departmentRepository,"student222","Computer Science");
        Enrollment en1 = new Enrollment(student,module1);
        Enrollment en2 = new Enrollment(student,module2);
        enrollmentRepository.persist(en1,en2);
        enrollmentRepository.flush();
        studentRepository.getEntityManager().clear();
        
        Student student1 = studentRepository.findById(student.getId());
        List<Enrollment> enrollments = student1.getStudentEnrollments();
        
        assert enrollments != null : "Teaching modules should not be null";
        assert enrollments.size() == 2 : "Expected 2 modules, found: " + enrollments.size();
    }

}
