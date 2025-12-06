package org.lms.Repository;

import java.util.List;
import java.util.UUID;

import org.lms.Model.Lecturer;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LecturerRepository implements PanacheRepositoryBase<Lecturer, UUID> {
    public Lecturer findByUserId(UUID userId) {
        return find("userId", userId).firstResult();
    }

    public List<Lecturer> findByDepartmentId(UUID departmentId) {
        return list("department.id", departmentId);
    }





}
