package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.lms.Dto.StudentDetailDto;
import org.lms.Model.*;
import org.lms.Model.Module;
import org.lms.Repository.AdminRepository;
import org.lms.Repository.DepartmentRepository;
import org.lms.Repository.LecturerRepository;
import org.lms.Repository.ModuleRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ModuleService {

    @Inject
    ModuleRepository moduleRepo;

    @Inject
    DepartmentRepository deptRepo;

    @Inject
    AdminRepository adminRepo;

    @Inject
    LecturerRepository lectRepo;

    @Inject
    StudentService studentService;

    @Transactional
    public Module createModule(String code, String name, int limit, UUID departmentId, UUID adminId) {
        Department dept = deptRepo.findById(departmentId);
        if (dept == null) {
            throw new NotFoundException("Department not found");
        }

        Admin admin = adminRepo.findById(adminId);
        if (admin == null) {
            throw new NotFoundException("Admin not found. Only Admins can create modules.");
        }

        Module module = new Module();
        module.setModule_code(code);
        module.setName(name);
        module.setLimit(limit);
        module.setDepartment(dept);
        module.setCreatedby(admin);

        moduleRepo.persist(module);
        return module;
    }


    @Transactional
    public void assignLecturer(UUID moduleId, UUID lecturerId) {
        Module module = moduleRepo.findById(moduleId);
        if (module == null) {
            throw new NotFoundException("Module not found");
        }

        Lecturer lecturer = lectRepo.findById(lecturerId);
        if (lecturer == null) {
            throw new NotFoundException("Lecturer not found");
        }

        module.setLecturer(lecturer);
    }


    public List<StudentDetailDto> getEnrolledStudents(UUID moduleId) {
        Module module = moduleRepo.findById(moduleId);
        if (module == null) {
            throw new NotFoundException("Module not found");
        }

        List<Enrollment> enrollments = module.getModuleEnrollments();

        if (enrollments == null) {
            return Collections.emptyList();
        }


        return enrollments.stream()
                .map(Enrollment::getStudent)
                .map(student -> studentService.getStudentDetails(student.getId())) // Convert to DTO using existing Service
                .collect(Collectors.toList());
    }

    public List<Module> getAllModules() {
        return moduleRepo.listAll();
    }
}