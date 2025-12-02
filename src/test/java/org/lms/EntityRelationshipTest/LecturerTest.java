//package org.lms.EntityRelationshipTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.lms.Model.Department;
//import org.lms.Model.Lecturer;
//import org.lms.Model.Module;
//import org.lms.Repository.AdminRepository;
//import org.lms.Repository.DepartmentRepository;
//import org.lms.Repository.LecturerRepository;
//import org.lms.Repository.ModuleRepository;
//import org.lms.Repository.UserRepository;
//
//import io.quarkus.test.junit.QuarkusTest;
//import jakarta.inject.Inject;
//import jakarta.transaction.Transactional;
//
//@QuarkusTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class LecturerTest {
//
//    @Inject
//    LecturerRepository lecturerRepository;
//
//    @Inject
//    UserRepository userRepository;
//
//    @Inject
//    DepartmentRepository departmentRepository;
//
//    @Inject
//    ModuleRepository moduleRepository;
//
//    @Inject
//    AdminRepository adminRepository;
//
//
//
//    @Test
//    @Transactional
//    public void createLecturerTestvalid(){
//
//        Lecturer lecturer1 = TestHelper.createLecturer(lecturerRepository, userRepository,departmentRepository, "lecturer","Computer Science");
//        lecturerRepository.persist(lecturer1);
//        lecturerRepository.flush();
//
//        lecturer1 = lecturerRepository.findById(lecturer1.getId());
//        assertNotNull(lecturer1);
//        assertNotNull(lecturer1.getUserId());
//    }
//
//    @Test
//    @Transactional
//    public void createLecturerWithWrongRole(){
//        Department foundCsDept = TestHelper.createDepartment(departmentRepository, "Computer Science");
//
//        Lecturer lecturer = new Lecturer("student123", foundCsDept);
//        lecturerRepository.persist(lecturer);
//        assertNotNull(lecturer);
//        assertEquals("student123", lecturer.getUserId());
//    }
//
//    @Test
//    @Transactional
//    public void createLecturerWithWrongRole2(){
//        Department foundCsDept = TestHelper.createDepartment(departmentRepository, "Computer Science");
//
//        Lecturer lecturer = new Lecturer("admin123", foundCsDept);
//        lecturerRepository.persist(lecturer);
//        assertNotNull(lecturer);
//        assertEquals("admin123", lecturer.getUserId());
//    }
//
//    @Test
//    @Transactional
//    public void listAllModulesOfLecturer(){
//        Lecturer lecturer1 = TestHelper.createLecturer(lecturerRepository, userRepository,departmentRepository, "lecturer2","Mathematics");
//        Module module1 = TestHelper.createModule(moduleRepository, lecturerRepository, departmentRepository, userRepository, adminRepository, "CS100", "Data Structure", 50, "lecturer2", "Mathematics", "admin123");
//        Module module2 = TestHelper.createModule(moduleRepository, lecturerRepository, departmentRepository, userRepository, adminRepository, "CS101", "Linear Algebra", 50, "lecturer2", "Mathematics", "admin123");
//
//        lecturer1 = lecturerRepository.findById(lecturer1.getId());
//        List<Module> teachingModules = lecturer1.getTeachingModules();
//
//        assert teachingModules != null : "Teaching modules should not be null";
//        assert teachingModules.size() == 2 : "Expected 2 modules, found: " + teachingModules.size();
//    }
//
//}
