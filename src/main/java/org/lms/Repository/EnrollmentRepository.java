package org.lms.Repository;

import java.util.List;
import java.util.UUID;
import org.lms.Model.Module;

import org.lms.Model.Enrollment;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.lms.Model.Student;

@ApplicationScoped
public class EnrollmentRepository implements PanacheRepositoryBase<Enrollment, UUID> {

    public long countByModuleId(UUID moduleId) {
        return count("module.id", moduleId);
    }



    public List<Module> findModulesByStudentId(UUID studentId) {
            return find("student.id", studentId).project(Module.class).list();
    }


    public List<Student> findStudentsByModuleId(UUID moduleId) {
        return find("module.id", moduleId).project(Student.class).list();
    }




}
