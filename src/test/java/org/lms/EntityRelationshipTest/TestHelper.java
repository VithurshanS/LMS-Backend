package org.lms.EntityRelationshipTest;

import org.lms.Model.*;
import org.lms.Model.Module;
import org.lms.Repository.AdminRepository;
import org.lms.Repository.DepartmentRepository;
import org.lms.Repository.LecturerRepository;
import org.lms.Repository.ModuleRepository;
import org.lms.Repository.UserRepository;
import java.util.UUID;

public class TestHelper {
    public static Department createDepartment(DepartmentRepository departmentRepository, String deptName) {
        Department existingDept = departmentRepository.find("name", deptName).firstResult();
        if (existingDept == null) {
            Department department = new Department(deptName);
            departmentRepository.persist(department);
            departmentRepository.flush();
            return departmentRepository.find("name", deptName).firstResult(); // Return with generated ID
        }
        return existingDept;
    }
    public static Lecturer createLecturer(LecturerRepository lecturerRepository, UserRepository userRepository, DepartmentRepository departmentRepository, String username, String deptName) {
        Department department = createDepartment(departmentRepository, deptName);
        Lecturer existingLecturer = lecturerRepository.find("userId", UUID.fromString(username.replaceAll("[^0-9a-fA-F-]", "0").substring(0, 8) + "-0000-0000-0000-000000000000")).firstResult();
        if (existingLecturer == null) {
            String userId = username.replaceAll("[^0-9a-fA-F-]", "0").substring(0, 8) + "-0000-0000-0000-000000000000";
            Lecturer lecturer = new Lecturer(userId, department);
            lecturerRepository.persist(lecturer);
            lecturerRepository.flush();
            return lecturer;
        }
        return existingLecturer;
    }

    public static Student createStudent(org.lms.Repository.StudentRepository studentRepository, UserRepository userRepository, DepartmentRepository departmentRepository, String username, String deptName) {
        Department department = createDepartment(departmentRepository, deptName);
        Student existingStudent = studentRepository.find("userId", username).firstResult();
        if (existingStudent == null) {
            Student student = new Student(username, department);
            studentRepository.persist(student);
            studentRepository.flush();
            return student;
        }
        return existingStudent;
    }

    public static Admin createAdmin(AdminRepository adminRepository, UserRepository userRepository, String username) {
        Admin existingAdmin = adminRepository.find("userId", UUID.fromString(username.replaceAll("[^0-9a-fA-F-]", "0").substring(0, 8) + "-0000-0000-0000-000000000000")).firstResult();
        if (existingAdmin == null) {
            String userId = username.replaceAll("[^0-9a-fA-F-]", "0").substring(0, 8) + "-0000-0000-0000-000000000000";
            Admin admin = new Admin(userId);
            adminRepository.persist(admin);
            adminRepository.flush();
            return admin;
        }
        return existingAdmin;
    }

    public static Module createModule(ModuleRepository moduleRepository, LecturerRepository lecturerRepository, DepartmentRepository departmentRepository, UserRepository userRepository, AdminRepository adminRepository, String moduleCode, String moduleName, int enrollmentLimit, String lecturerUsername, String deptName, String adminUsername) {
        Lecturer lecturer = createLecturer(lecturerRepository, userRepository, departmentRepository, lecturerUsername, deptName);
        
        Department department = createDepartment(departmentRepository, deptName);
        Admin admin = createAdmin(adminRepository, userRepository, adminUsername);

        Module existingModule = moduleRepository.find("module_code", moduleCode).firstResult();
        if (existingModule == null) {
            Module module = new Module(moduleCode, moduleName, enrollmentLimit, department, lecturer, admin);
            moduleRepository.persist(module);
            moduleRepository.flush();
            lecturerRepository.getEntityManager().clear();
            return moduleRepository.find("module_code", moduleCode).firstResult();

        }
        return existingModule;
    }

    public static Enrollment createEnrollment(org.lms.Repository.EnrollmentRepository enrollmentRepository, ModuleRepository moduleRepository, org.lms.Repository.StudentRepository studentRepository, String moduleCode, String studentUsername) {
        Module module = moduleRepository.find("module_code", moduleCode).firstResult();
        Student student = studentRepository.find("userId", studentUsername).firstResult();

        if (module != null && student != null) {
            Enrollment existingEnrollment = enrollmentRepository.find("module.id = ?1 and student.id = ?2", module.getId(), student.getId()).firstResult();
            if (existingEnrollment == null) {
                Enrollment enrollment = new Enrollment(student, module);
                enrollmentRepository.persist(enrollment);
                enrollmentRepository.flush();
                return enrollment;
            }
            return existingEnrollment;
        }
        return null;
    }


    public static void setupSampleUsers(UserRepository userRepository) {
    }

    public static void setupSampleDepartments(DepartmentRepository departmentRepository) {
        Department csDept = new Department("Computer Science");
        Department mathDept = new Department("Mathematics");
        Department phyDept = new Department("Physics");

        departmentRepository.persist(csDept);
        departmentRepository.persist(mathDept);
        departmentRepository.persist(phyDept);
        departmentRepository.flush();
    }

    public static void setupSampleLecturers(LecturerRepository lecturerRepository, UserRepository userRepository, DepartmentRepository departmentRepository) {
        Department csDept = departmentRepository.find("name", "Computer Science").firstResult();
        if (csDept != null) {
            Lecturer lecturer = new Lecturer("lecturer123-0000-0000-0000-000000000000", csDept);
            lecturerRepository.persist(lecturer);
            lecturerRepository.flush();
        }
    }

    public static void setupSampleModules(ModuleRepository moduleRepository, LecturerRepository lecturerRepository,DepartmentRepository departmentRepository, UserRepository userRepository,AdminRepository adminRepository) {
        Lecturer lecturer = lecturerRepository.findAll().firstResult();
        Department csDept = departmentRepository.find("name", "Computer Science").firstResult();
        Admin admin = adminRepository.findAll().firstResult();

        if (lecturer != null && csDept != null && admin != null) {
            Module module1 = new Module("MA102", "Linear Algebra",30, csDept,lecturer,admin);
            Module module2 = new Module("CS102", "Data Structures",50, csDept,lecturer,admin);

            moduleRepository.persist(module1);
            moduleRepository.persist(module2);
            moduleRepository.flush();
        }
    }

    
}
