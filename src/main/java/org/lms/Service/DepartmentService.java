package org.lms.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.lms.Dto.DepartmentDetailDto;
import org.lms.Dto.ModuleDetailDto;
import org.lms.Model.Department;
import org.lms.Repository.DepartmentRepository;
import org.lms.Repository.ModuleRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DepartmentService {

    @Inject
    DepartmentRepository deptRepo;

    @Inject
    ObjectMapper mapper;


    @Transactional
    public Department createDepartment(String name) {
        Department dept = new Department(name);
        deptRepo.persist(dept);
        return dept;
    }

    public List<DepartmentDetailDto> getAllDepartments() {
        List<Department> deps = deptRepo.listAll();

        return deps.stream()
                .map(d -> mapper.convertValue(d, DepartmentDetailDto.class))
                .toList();
    }
    public Department getDepartmentById(UUID id) {
        return deptRepo.findById(id);
    }

}