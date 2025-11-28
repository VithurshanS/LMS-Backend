package org.lms.EntityRelationshipTest;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.lms.Model.Admin;
import org.lms.Model.Lecturer;
import org.lms.Model.User;
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
public class AdminTest {
    
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
    public void createAdminTestValid(){
        User foundAdminUser = TestHelper.createAdminUser(userRepository, "admin_valid_123","admin@lms.com");
        
        Admin admin = new Admin(foundAdminUser);
        adminRepository.persist(admin);
        adminRepository.flush();
        adminRepository.getEntityManager().clear();

        Admin admin1 = adminRepository.findById(admin.getId());
        assert(admin1 != null);
        assert(admin1.getUser().getEmail().equals("admin@lms.com"));
        assert(admin1.getUser().getUsername().equals("admin_valid_123"));
    }

    @Test
    @Transactional
    public void createAdminWithWrongRole(){
        User foundStudentUser = TestHelper.createStudentUser(userRepository, "student_wrong_123","student123@lms.com");
  
        try {
            Admin admin = new Admin(foundStudentUser);
            assert false : "Should throw IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    @Transactional
    public void createAdminWithWrongRole2(){
        User foundLecturerUser = TestHelper.createLecturerUser(userRepository, "lecturer_wrong_123","lecturer123@lms.com");
  
        try {
            Admin admin = new Admin(foundLecturerUser);
            assert false : "Should throw IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    @Transactional
    public void listAllCreatedModules(){
        Admin admin = TestHelper.createAdmin(adminRepository, userRepository, "admin_modules_123");
        Lecturer lecturer1 = TestHelper.createLecturer(lecturerRepository, userRepository,departmentRepository, "lecturer2","Mathematics");
        org.lms.Model.Module module1 = TestHelper.createModule(moduleRepository, lecturerRepository, departmentRepository, userRepository, adminRepository, "CS100", "Data Structure", 50, "lecturer2", "Mathematics", "admin_modules_123");
        org.lms.Model.Module module2 = TestHelper.createModule(moduleRepository, lecturerRepository, departmentRepository, userRepository, adminRepository, "CS101", "Linear Algebra", 50, "lecturer2", "Mathematics", "admin_modules_123");
        
        adminRepository.getEntityManager().clear();
        
        Admin admin1 = adminRepository.findById(admin.getId());
        List<org.lms.Model.Module> createdModules = admin1.getCreatedModules();
        
        assert createdModules != null : "Created modules should not be null";
        assert createdModules.size() == 2 : "Expected 2 modules, found: " + createdModules.size();
        boolean foundModule1 = false;
        boolean foundModule2 = false;
        for (org.lms.Model.Module module : createdModules) {
            if (module.getModule_code().equals("CS100")) {
                foundModule1 = true;
            } else if (module.getModule_code().equals("CS101")) {
                foundModule2 = true;
            }
        }
        assert foundModule1 : "CS100 module should be found in created modules";
        assert foundModule2 : "CS101 module should be found in created modules";
    }

}
