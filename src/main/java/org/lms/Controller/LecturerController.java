package org.lms.Controller;


import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.ApproveRequest;
import org.lms.Dto.ModuleDetailDto;
import org.lms.Dto.UserResponseDto;
import org.lms.Service.LecturerService;
import org.lms.Service.ModuleService;
import org.lms.Service.UserService;

import java.util.List;
import java.util.UUID;




@Path("/api/lecturer")
public class LecturerController {
    @Inject
    ModuleService moduleService;

    @Inject
    LecturerService lecturerService;

    @Inject
    UserService userService;

    @Inject
    SecurityIdentity identity;


    @PATCH
    @Path("/approve-lecturer") //admin
    @Consumes(MediaType.APPLICATION_JSON)
    public Response approve(ApproveRequest request){

        return userService.approveUser(request.lecturerId);
    }

    @RolesAllowed("admin")
    @GET
    @Path("/all") //admin
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLecturers() {
        try {
            List<UserResponseDto> lecturers = lecturerService.getAllLecturers2();
            return Response.ok(lecturers).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching lecturers: " + e.getMessage()).build();
        }
    }

    @RolesAllowed("admin")
    @GET
    @Path("/departmentId/{id}") //admin
    public Response getAllLecturersbyDeptId(@PathParam("id") UUID deptid) {
        try {
            List<UserResponseDto> lecturers = lecturerService.getLecturerDetailsbyDepartmentId(deptid);
            return Response.ok(lecturers).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching lecturers: " + e.getMessage()).build();
        }
    }


    @RolesAllowed({"admin","lecturer","student"})
    @GET
    @Path("/id/{id}") //lecturer +admin
    public Response getLecturerById(@PathParam("id") UUID lecturerId) {
        try {
            UserResponseDto dto = lecturerService.getLecturerDetails2(lecturerId);
            if (dto == null) {
                return Response.status(404).entity("Lecturer not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching lecturer: " + e.getMessage()).build();
        }
    }


    @RolesAllowed("lecturer")
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String accessLectureResource(){
        return "I am a lecturer Resource";
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public

}
