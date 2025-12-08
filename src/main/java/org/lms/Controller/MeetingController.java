package org.lms.Controller;


import io.quarkus.oidc.OidcTenantConfig;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Service.MeetingService;

import java.util.UUID;

@Path("/api/meet")
public class MeetingController {

    @Inject
    MeetingService meetingService;

    @RolesAllowed({"lecturer","student"})
    @GET
    @Path("/create/{id}")
    public Response createmeeting(@PathParam("id") UUID moduleId){
        try {
            return Response.ok(meetingService.createMeeting(moduleId)).build();
        } catch (Exception e) {
            return Response.status(400).entity(e.toString()).build();
        }


    }
}
