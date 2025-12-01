package org.lms.EntityRelationshipTest;

import org.lms.Model.*;
import org.lms.Model.Module;
import org.lms.Model.UserDB;
import org.lms.Repository.AdminRepository;
import org.lms.Repository.DepartmentRepository;
import org.lms.Repository.LecturerRepository;
import org.lms.Repository.ModuleRepository;
import org.lms.Repository.UserRepository;

public class TestHelper {
    public static UserDB createAdminUser(UserRepository userRepository, String username, String email) {
        UserDB existingUserDB = userRepository.find("username", username).firstResult();
        if (existingUserDB == null) {
            // Split username as firstName lastName or use default
            String[] nameParts = username.split("_");
            String firstName = nameParts.length > 0 ? nameParts[0] : username;
            String lastName = nameParts.length > 1 ? nameParts[1] : "Admin";
            UserDB userDB = new UserDB(firstName, lastName, email, UserRole.ADMIN, username, "password");
            userRepository.persist(userDB);
            userRepository.flush();
            return userRepository.find("username", username).firstResult(); // Return with generated ID
        }
        return existingUserDB;
    }
    public static UserDB createLecturerUser(UserRepository userRepository, String username, String email) {
        UserDB existingUserDB = userRepository.find("username", username).firstResult();
        if (existingUserDB == null) {
            // Split username as firstName lastName or use default
            String[] nameParts = username.split("_");
            String firstName = nameParts.length > 0 ? nameParts[0] : username;
            String lastName = nameParts.length > 1 ? nameParts[1] : "Lecturer";
            UserDB userDB = new UserDB(firstName, lastName, email, UserRole.LECTURER, username, "password");
            userRepository.persist(userDB);
            userRepository.flush();
            return userRepository.find("username", username).firstResult(); // Return with generated ID
        }
        return existingUserDB;
    }
    public static UserDB createStudentUser(UserRepository userRepository, String username, String email) {
        UserDB existingUserDB = userRepository.find("username", username).firstResult();
        if (existingUserDB == null) {
            // Split username as firstName lastName or use default
            String[] nameParts = username.split("_");
            String firstName = nameParts.length > 0 ? nameParts[0] : username;
            String lastName = nameParts.length > 1 ? nameParts[1] : "Student";
            UserDB userDB = new UserDB(firstName, lastName, email, UserRole.STUDENT, username, "password");
            userRepository.persist(userDB);
            userRepository.flush();
            return userRepository.find("username", username).firstResult(); // Return with generated ID
        }
        return existingUserDB;
    }
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
        UserDB userDB = createLecturerUser(userRepository, username, username + "@lms.com");
        Department department = createDepartment(departmentRepository, deptName);
        Lecturer existingLecturer = lecturerRepository.find("user.username", username).firstResult();
        if (existingLecturer == null) {
            Lecturer lecturer = new Lecturer(userDB, department);
            lecturerRepository.persist(lecturer);
            lecturerRepository.flush();
            return lecturerRepository.find("user.username", username).firstResult(); // Return with generated ID
        }
        return existingLecturer;
    }

    public static Student createStudent(org.lms.Repository.StudentRepository studentRepository, UserRepository userRepository, DepartmentRepository departmentRepository, String username, String deptName) {
        UserDB userDB = createStudentUser(userRepository, username, username + "@lms.com");
        Department department = createDepartment(departmentRepository, deptName);
        Student existingStudent = studentRepository.find("userId", username).firstResult();
        if (existingStudent == null) {
            Student student = new Student(username, department);
            studentRepository.persist(student);
            studentRepository.flush();
            return studentRepository.find("userId", username).firstResult();
        }
        return existingStudent;
    }

    public static Admin createAdmin(AdminRepository adminRepository, UserRepository userRepository, String username) {
        UserDB userDB = createAdminUser(userRepository, username, username + "@lms.com");
        Admin existingAdmin = adminRepository.find("user.username", username).firstResult();
        if (existingAdmin == null) {
            Admin admin = new Admin(userDB);
            adminRepository.persist(admin);
            adminRepository.flush();
            return adminRepository.find("user.username", username).firstResult(); // Return with generated ID
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
        Student student = studentRepository.find("user.username", studentUsername).firstResult();

        Enrollment existingEnrollment = enrollmentRepository.find("module.id = ?1 and student.id = ?2", module.getId(), student.getId()).firstResult();
        if (existingEnrollment == null) {
            Enrollment enrollment = new Enrollment(student, module);
            enrollmentRepository.persist(enrollment);
            enrollmentRepository.flush();
            return enrollmentRepository.find("module.id = ?1 and student.id = ?2", module.getId(), student.getId()).firstResult(); // Return with generated ID
        }
        return existingEnrollment;
    }


    public static void setupSampleUsers(UserRepository userRepository) {
        UserDB adminUserDB = new UserDB("Admin", "User", "admin@lms.com", UserRole.ADMIN, "admin123", "password123");
        UserDB lecturerUserDB = new UserDB("Lecturer", "User", "lecturer@lms.com", UserRole.LECTURER, "lecturer123", "password123");
        UserDB studentUserDB = new UserDB("Student", "User", "student@lms.com", UserRole.STUDENT, "student123", "password123");

        userRepository.persist(adminUserDB);
        userRepository.persist(lecturerUserDB);
        userRepository.persist(studentUserDB);
        userRepository.flush();
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
        UserDB lecturerUserDB = userRepository.find("username", "lecturer123").firstResult();
        Department csDept = departmentRepository.find("name", "Computer Science").firstResult();

        Lecturer lecturer = new Lecturer(lecturerUserDB, csDept);
        lecturerRepository.persist(lecturer);
        lecturerRepository.flush();
    }

    public static void setupSampleModules(ModuleRepository moduleRepository, LecturerRepository lecturerRepository,DepartmentRepository departmentRepository, UserRepository userRepository,AdminRepository adminRepository) {
        Lecturer lecturer = lecturerRepository.find("user.username", "lecturer123").firstResult();
        Department csDept = departmentRepository.find("name", "Computer Science").firstResult();
        Admin admin = adminRepository.find("user.username", "admin123").firstResult();

        Module module1 = new Module("MA102", "Linear Algebra",30, csDept,lecturer,admin);

        Module module2 = new Module("CS102", "Data Structures",50, csDept,lecturer,admin);

        moduleRepository.persist(module1);
        moduleRepository.persist(module2);
        moduleRepository.flush();
    }

    
}
