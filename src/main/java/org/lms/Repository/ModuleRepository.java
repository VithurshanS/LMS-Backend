package org.lms.Repository;

import java.util.List;
import java.util.UUID;

import org.lms.Model.Module;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModuleRepository implements PanacheRepositoryBase<Module, UUID> {
    public List<Module> findByDepartmentId(UUID departmentId) {
        return list("department.id", departmentId);
    }


    public List<Module> findByLecturerId(UUID lecturerId) {
        return list("lecturer.id", lecturerId);
    }

    public boolean hasEnrollmentForStudentAndModule(UUID studentId, UUID moduleId) {
        long count = find("SELECT COUNT(m) FROM Module m JOIN m.moduleEnrollments e WHERE m.id = ?1 AND e.student.id = ?2", 
                         moduleId, studentId).count();
        return count > 0;
    }

}
