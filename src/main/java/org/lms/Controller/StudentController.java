package org.lms.Controller;


import io.quarkus.security.identity.SecurityIdentity;
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

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String accessStudentResource(){
        return "I am a student Resource";
    }

    @Inject
    EnrollmentService enrollmentService;



    @POST
    @Path("/enroll")
    public Response enrollStudent(EnrollRequest req) {
        try {
            Enrollment enr = enrollmentService.enrollStudent(req.studentId, req.moduleId);
            return Response.status(201).entity(enr).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }


    @GET
    @Path("/students-by-module/{id}") //lecturer +admin +student
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
    @GET
    @Path("/enrollments/{id}") //lecturer +admin +student
    public Response getLecturerById(@PathParam("id") UUID studentId) {
        try {
            List<ModuleDetailDto> dto = moduleService.getModulesByStudentId(studentId);
            if (dto == null) {
                return Response.status(404).entity("Lecturer not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching lecturer: " + e.getMessage()).build();
        }
    }




}
