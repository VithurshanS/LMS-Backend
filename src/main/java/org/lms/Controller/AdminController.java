package org.lms.Controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.LecturerDetailDto;
import org.lms.Dto.StudentDetailDto;
import org.lms.Service.LecturerService;
import org.lms.Service.StudentService;
import org.lms.Service.UserService;

import java.util.List;
import java.util.UUID;

@Path("/admin")
public class AdminController {
    @Inject
    UserService userService;

    @Inject
    LecturerService lecturerService;

    @Inject
    StudentService studentService;

    @PUT
    @Path("/approve-lecturer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response approve(ApproveRequest request){
        return userService.approveUser(request.userId);
    }

    @GET
    @Path("/all-lecturers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLecturers() {
        try {
            List<LecturerDetailDto> lecturers = lecturerService.getAllLecturers();
            return Response.ok(lecturers).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error fetching lecturers: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/all-students")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudents() {
        try {
            List<StudentDetailDto> students = studentService.getAllStudents();
            return Response.ok(students).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error fetching students: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/lecturer/{id}")
    public Response getLecturerById(@PathParam("id") UUID lecturerId) {
        try {
            LecturerDetailDto dto = lecturerService.getLecturerDetails(lecturerId);
            if (dto == null) {
                return Response.status(404).entity("Lecturer not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching lecturer: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/student/{id}")
    public Response getStudentById(@PathParam("id") UUID studentId) {
        try {
            StudentDetailDto dto = studentService.getStudentDetails(studentId);
            if (dto == null) {
                return Response.status(404).entity("Student not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching student: " + e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/control-user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response controllUser(ControlRequest cr){
        try{
            userService.controllUserAccess(cr.userId, cr.control);
            return Response.ok("user modified").build();
        }catch (Exception e){
            return Response.notModified(e.getMessage()).build();
        }

    }


//    @GET
//    @Path("/get-lecturers")
//    public Response getLecturers(){return }
}
