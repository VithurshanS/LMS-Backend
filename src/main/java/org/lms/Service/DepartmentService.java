package org.lms.Service;

import java.util.List;
import java.util.UUID;

import org.lms.Dto.DepartmentDetailDto;
import org.lms.Model.Department;
import org.lms.Repository.DepartmentRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DepartmentService {

    @Inject
    DepartmentRepository deptRepo;

    @Inject
    ObjectMapper mapper;


    @Transactional
    public DepartmentDetailDto createDepartment(String name) {
        Department dept = new Department(name);
        deptRepo.persist(dept);
        return mapper.convertValue(dept,DepartmentDetailDto.class);
    }

    public List<DepartmentDetailDto> getAllDepartments() {
        List<Department> deps = deptRepo.listAll();

        return deps.stream()
                .map(d -> mapper.convertValue(d, DepartmentDetailDto.class))
                .toList();
    }

    public DepartmentDetailDto getDepartmentById(UUID id) {
        Department dept = deptRepo.findById(id);
        if (dept == null) {
            return null;
        }
        return mapper.convertValue(dept, DepartmentDetailDto.class);
    }

    public Department getDepartmentEntityById(UUID id) {
        return deptRepo.findById(id);
    }

}