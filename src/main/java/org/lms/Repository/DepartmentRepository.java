package org.lms.Repository;

import java.util.List;
import java.util.UUID;

import org.lms.Model.Department;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepositoryBase<Department, UUID> {

}
