package org.lms.Controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.*;
import org.lms.Model.Department;
import org.lms.Service.*;

import java.util.List;
import java.util.UUID;


@Path("/api/admin")
public class AdminController {


    @RolesAllowed("admin")
    @GET
    @Path("/test") //admin
    @Produces(MediaType.TEXT_PLAIN)
    public String testAdminResourse(){
        return "I am a admin resource";
    }




//    @GET
//    @Path("/get-lecturers")
//    public Response getLecturers(){return }
}
