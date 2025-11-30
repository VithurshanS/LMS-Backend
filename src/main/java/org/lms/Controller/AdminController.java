package org.lms.Controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.ApproveRequest;
import org.lms.Service.UserService;

@Path("/admin")
public class AdminController {
    @Inject
    UserService userService;

    @POST
    @Path("/approve-lecturer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response approve(ApproveRequest request){
        return userService.approveLecturer(request.userId);
    }
}
