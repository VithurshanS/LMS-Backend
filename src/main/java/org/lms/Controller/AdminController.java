package org.lms.Controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;



@Path("/api/admin")
public class AdminController {


    @RolesAllowed("admin")
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String testAdminResourse(){
        return "I am a admin resource";
    }




//    @GET
//    @Path("/get-lecturers")
//    public Response getLecturers(){return }
}
