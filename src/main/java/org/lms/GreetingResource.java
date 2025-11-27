package org.lms;

import org.lms.Model.Department;
import org.lms.Repository.DepartmentRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;



@Path("/hello")
public class GreetingResource {
    
    @Inject
    DepartmentRepository departmentRepository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String hello() {
        Department dept = new Department();
        dept.setName("Human Resour");
        departmentRepository.persist(dept);
        return "Hello from Quarkus REST";

    }
}
