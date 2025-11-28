package org.lms.EntityRelationshipTest;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.lms.Model.Admin;
import org.lms.Model.Department;
import org.lms.Model.Enrollment;
import org.lms.Model.Lecturer;
import org.lms.Model.Module;
import org.lms.Model.Student;
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
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModuleTest {
    
    @Inject
    EntityManager em;

    @Inject
    ModuleRepository moduleRepository;

    @Inject
    LecturerRepository lecturerRepository;

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    AdminRepository adminRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    StudentRepository studentRepository;

    @Inject
    EnrollmentRepository enrollmentRepository;

    @Test
    @Transactional
    public void createModuleTestValid(){
        Lecturer lecturer = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer_module1", "Computer Science");
        Department department = TestHelper.createDepartment(departmentRepository, "Computer Science");
        Admin admin = TestHelper.createAdmin(adminRepository, userRepository, "admin_module1");

        Module module = new Module("CS102", "Data Structures", 30, department, lecturer, admin);
        moduleRepository.persist(module);
        moduleRepository.flush();
        moduleRepository.getEntityManager().clear();

        Module foundModule = moduleRepository.findById(module.getId());
        assert foundModule != null;
        assert foundModule.getModule_code().equals("CS102");
        assert foundModule.getName().equals("Data Structures");
        assert foundModule.getLimit() == 30;
        assert foundModule.getDepartment().getName().equals("Computer Science");
        assert foundModule.getLecturer().getUser().getUsername().equals("lecturer_module1");
        assert foundModule.getCreatedby().getUser().getUsername().equals("admin_module1");
    }

    @Test
    @Transactional
    public void testModuleCodeUniqueness(){
        Lecturer lecturer = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer_unique1", "Computer Science");
        Department department = TestHelper.createDepartment(departmentRepository, "Computer Science");
        Admin admin = TestHelper.createAdmin(adminRepository, userRepository, "admin_unique1");

        // Create first module
        Module module1 = new Module("CS102", "Algorithms", 25, department, lecturer, admin);
        moduleRepository.persist(module1);
        moduleRepository.flush();

        // Try to create second module with same code
        Module module2 = new Module("CS102", "Different Course", 30, department, lecturer, admin);
        moduleRepository.persist(module2);
        
        PersistenceException exception = null;
        try {
            moduleRepository.flush();
        } catch (PersistenceException e) {
            exception = e;
        }
        
        assert exception != null : "Should throw PersistenceException for duplicate module code";
    }

    @Test
    @Transactional
    public void testModuleWithNullValues(){
        Lecturer lecturer = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer_null", "Computer Science");
        Department department = TestHelper.createDepartment(departmentRepository, "Computer Science");
        Admin admin = TestHelper.createAdmin(adminRepository, userRepository, "admin_null");
        try {
            Module module = new Module();
            module.setModule_code(null);
            moduleRepository.persist(module);
            moduleRepository.flush();
            assert false : "Should not allow null module code";
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @Transactional
    public void testModuleEnrollmentLimit(){
        Lecturer lecturer = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer_limit", "Computer Science");
        Department department = TestHelper.createDepartment(departmentRepository, "Computer Science");
        Admin admin = TestHelper.createAdmin(adminRepository, userRepository, "admin_limit");

        // Create module with limit of 2
        Module module = new Module("CS103", "Test Course", 2, department, lecturer, admin);
        moduleRepository.persist(module);
        moduleRepository.flush();

        // Create students and enroll them
        Student student1 = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student_limit1", "Computer Science");
        Student student2 = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student_limit2", "Computer Science");

        Enrollment en1 = new Enrollment(student1, module);
        Enrollment en2 = new Enrollment(student2, module);
        enrollmentRepository.persist(en1);
        enrollmentRepository.persist(en2);
        enrollmentRepository.flush();

        moduleRepository.getEntityManager().clear();
        Module foundModule = moduleRepository.findById(module.getId());
        List<Enrollment> enrollments = foundModule.getModuleEnrollments();
        
        assert enrollments != null;
        assert enrollments.size() == 2 : "Expected 2 enrollments, found: " + enrollments.size();
        assert foundModule.getLimit() == 2;
    }

    @Test
    @Transactional
    public void testModuleDepartmentRelationship(){
        Lecturer lecturer = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer_dept", "Mathematics");
        Department mathDept = TestHelper.createDepartment(departmentRepository, "Mathematics");
        Admin admin = TestHelper.createAdmin(adminRepository, userRepository, "admin_dept");

        Module module = new Module("MA201", "Calculus", 40, mathDept, lecturer, admin);
        moduleRepository.persist(module);
        moduleRepository.flush();
        moduleRepository.getEntityManager().clear();

        Module foundModule = moduleRepository.findById(module.getId());
        assert foundModule.getDepartment() != null;
        assert foundModule.getDepartment().getName().equals("Mathematics");
        assert foundModule.getDepartment().getId().equals(mathDept.getId());
    }

    @Test
    @Transactional
    public void testModuleLecturerRelationship(){
        Lecturer lecturer = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer_rel", "Physics");
        Department department = TestHelper.createDepartment(departmentRepository, "Physics");
        Admin admin = TestHelper.createAdmin(adminRepository, userRepository, "admin_rel");

        Module module = new Module("PH101", "Physics I", 35, department, lecturer, admin);
        moduleRepository.persist(module);
        moduleRepository.flush();
        moduleRepository.getEntityManager().clear();

        Module foundModule = moduleRepository.findById(module.getId());
        assert foundModule.getLecturer() != null;
        assert foundModule.getLecturer().getUser().getUsername().equals("lecturer_rel");
        assert foundModule.getLecturer().getId().equals(lecturer.getId());
    }

    @Test
    @Transactional
    public void testModuleAdminRelationship(){
        Lecturer lecturer = TestHelper.createLecturer(lecturerRepository, userRepository, departmentRepository, "lecturer_admin", "Chemistry");
        Department department = TestHelper.createDepartment(departmentRepository, "Chemistry");
        Admin admin = TestHelper.createAdmin(adminRepository, userRepository, "admin_creator");

        Module module = new Module("CH101", "Chemistry I", 45, department, lecturer, admin);
        moduleRepository.persist(module);
        moduleRepository.flush();
        moduleRepository.getEntityManager().clear();

        Module foundModule = moduleRepository.findById(module.getId());
        assert foundModule.getCreatedby() != null;
        assert foundModule.getCreatedby().getUser().getUsername().equals("admin_creator");
        assert foundModule.getCreatedby().getId().equals(admin.getId());
    }

    @Test
    @Transactional
    public void testModuleEnrollmentsRelationship(){
        Module module = TestHelper.createModule(moduleRepository, lecturerRepository, departmentRepository, userRepository, adminRepository, 
                                                "CS104", "Software Engineering", 25, "lecturer_enroll", "Computer Science", "admin_enroll");
        
        Student student1 = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student_enroll1", "Computer Science");
        Student student2 = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student_enroll2", "Computer Science");
        Student student3 = TestHelper.createStudent(studentRepository, userRepository, departmentRepository, "student_enroll3", "Computer Science");

        Enrollment en1 = new Enrollment(student1, module);
        Enrollment en2 = new Enrollment(student2, module);
        Enrollment en3 = new Enrollment(student3, module);
        
        enrollmentRepository.persist(en1);
        enrollmentRepository.persist(en2);
        enrollmentRepository.persist(en3);
        enrollmentRepository.flush();
        moduleRepository.getEntityManager().clear();

        Module foundModule = moduleRepository.findById(module.getId());
        List<Enrollment> enrollments = foundModule.getModuleEnrollments();
        
        assert enrollments != null : "Module enrollments should not be null";
        assert enrollments.size() == 3 : "Expected 3 enrollments, found: " + enrollments.size();
        
        // Verify all enrollments are for this module
        for (Enrollment enrollment : enrollments) {
            assert enrollment.getModule().getId().equals(module.getId());
        }
    }
}
