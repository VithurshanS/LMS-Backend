package org.lms.Controller;


import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.ModuleDetailDto;
import org.lms.Service.ModuleService;

import java.util.List;
import java.util.UUID;




@Path("/api/lecturer")
public class LecturerController {
    @Inject
    ModuleService moduleService;

    @Inject
    SecurityIdentity identity;

    @GET
    @Path("/getmodule-lect/{id}") //admin + lecturer
    public Response getModulesbyLectId(@PathParam("id") UUID lectId){
        try{
            List<ModuleDetailDto> modules = moduleService.getModulesByLectId(lectId);
            return Response.ok(modules).build();
        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

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
