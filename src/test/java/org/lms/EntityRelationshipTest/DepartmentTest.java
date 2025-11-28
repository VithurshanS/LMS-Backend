package org.lms.EntityRelationshipTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.lms.Model.Department;
import java.util.*;
import org.lms.Model.Student;
import org.lms.Model.Lecturer;
import org.lms.Repository.AdminRepository;
import org.lms.Repository.DepartmentRepository;
import org.lms.Repository.EnrollmentRepository;
import org.lms.Repository.LecturerRepository;
import org.lms.Repository.ModuleRepository;
import org.lms.Repository.StudentRepository;
import org.lms.Repository.UserRepository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepartmentTest {
    @Inject
    EntityManager em;
    @Inject
    LecturerRepository lecturerRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    ModuleRepository moduleRepository;

    @Inject
    AdminRepository adminRepository;

    @Inject
    StudentRepository studentRepository;

    @Test
    @Transactional
    public void DepartmentCreationTest(){
        Department dept = new Department("Mathematics");
        em.persist(dept);
    }

    @Test
    @Transactional
    public void getStudentlist(){
        Department dept = TestHelper.createDepartment(departmentRepository,"Computer Science");
        Student st1 = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student1", "Computer Science");
        Student st2 = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student2", "Computer Science");
        Student st3 = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student3", "Computer Science");

        departmentRepository.getEntityManager().clear();
        Department dept_fetched = departmentRepository.findById(dept.getId());
        List<Student> allstudent = dept_fetched.getStudentList();

        assert allstudent.size() == 3;


    }

    @Test
    @Transactional
    public void getLecturelist(){
        Department dept = TestHelper.createDepartment(departmentRepository,"Computer Sciences");
        Lecturer st1 = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer1", "Computer Sciences");
        Lecturer st2 = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer2", "Computer Sciences");
        Lecturer st3 = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer3", "Computer Sciences");

        departmentRepository.getEntityManager().clear();
        Department dept_fetched = departmentRepository.findById(dept.getId());
        List<Lecturer> alllecturer = dept_fetched.getLecturerList();

        assert alllecturer.size() == 3;


    }


    
}
