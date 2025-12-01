package org.lms.Controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.ApproveRequest;
import org.lms.Dto.LecturerDetailDto;
import org.lms.Service.LecturerService;
import org.lms.Service.UserService;

import java.util.List;

@Path("/admin")
public class AdminController {
    @Inject
    UserService userService;

    @Inject
    LecturerService lecturerService;

    @POST
    @Path("/approve-lecturer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response approve(ApproveRequest request){
        return userService.approveLecturer(request.userId);
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

//    @GET
//    @Path("/get-lecturers")
//    public Response getLecturers(){return }
}
