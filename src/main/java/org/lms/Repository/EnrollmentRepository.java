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
        List<Enrollment> enrollments = find("student.id", studentId).list();
        return enrollments.stream()
                .map(Enrollment::getModule)
                .toList();
    }


    public List<Student> findStudentsByModuleId(UUID moduleId) {
        List<Enrollment> enrollments = find("module.id", moduleId).list();
        return enrollments.stream()
                .map(Enrollment::getStudent)
                .toList();
    }




}
