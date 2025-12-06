package org.lms.Repository;

import java.util.List;
import java.util.UUID;

import org.lms.Model.Lecturer;
import org.lms.Model.Student;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentRepository implements PanacheRepositoryBase<Student, UUID> {
    public Student findByUserId(UUID userId) {
        return find("userId", userId).firstResult();
    }

    public List<Student> findByDepartmentId(UUID departmentId) {
        return list("department.id", departmentId);
    }
}
