package org.lms.Controller;


import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.EnrollRequest;
import org.lms.Dto.ModuleDetailDto;
import org.lms.Dto.UserResponseDto;
import org.lms.Model.Enrollment;
import org.lms.Service.EnrollmentService;
import org.lms.Service.ModuleService;
import org.lms.Service.StudentService;

import java.util.List;
import java.util.UUID;

@Path("/api/student")
public class StudentController {

    @Inject
    SecurityIdentity identity;

    @Inject
    ModuleService moduleService;

    @Inject
    StudentService studentService;

    @RolesAllowed("student")
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String accessStudentResource(){
        return "I am a student Resource";
    }

    @Inject
    EnrollmentService enrollmentService;





    @RolesAllowed({"lecturer","admin"})
    @GET
    @Path("/moduleId/{id}") //lecturer +admin +student
    public Response getStudentsByModuleId(@PathParam("id") UUID moduleId) {
        try {
            List<UserResponseDto> dto = studentService.getStudentsByModuleId(moduleId);
            if (dto == null) {
                return Response.status(404).entity("students not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching students: " + e.getMessage()).build();
        }
    }

    @RolesAllowed({"admin"})
    @GET
    @Path("/all") //admin + lecturers
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudents() {
        try {
            List<UserResponseDto> students = studentService.getAllStudents2();
            return Response.ok(students).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error fetching students: " + e.getMessage()).build();
        }
    }
    @RolesAllowed({"admin","lecturer"})
    @GET
    @Path("/departmentId/{id}") //admin
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudentssbyDeptId(@PathParam("id") UUID deptid) {
        try {
            List<UserResponseDto> students = studentService.getStudentDetailsbyDepartmentId(deptid);
            return Response.ok(students).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error fetching students: " + e.getMessage()).build();
        }
    }





    @RolesAllowed({"admin","student","lecturer"})
    @GET
    @Path("/id/{id}") //lecturer + admin +student
    public Response getStudentById(@PathParam("id") UUID studentId) {
        try {
            UserResponseDto dto = studentService.getStudentDetails2(studentId);
            if (dto == null) {
                return Response.status(404).entity("Student not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching student: " + e.getMessage()).build();
        }
    }




}
