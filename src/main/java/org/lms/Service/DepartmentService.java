package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.lms.Model.Department;
import org.lms.Repository.DepartmentRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DepartmentService {

    @Inject
    DepartmentRepository deptRepo;

    @Transactional
    public Department createDepartment(String name) {
        Department dept = new Department(name);
        deptRepo.persist(dept);
        return dept;
    }

    public List<Department> getAllDepartments() {
        return deptRepo.listAll();
    }

    public Department getDepartmentById(UUID id) {
        return deptRepo.findById(id);
    }
}