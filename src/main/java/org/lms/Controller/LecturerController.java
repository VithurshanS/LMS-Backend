package org.lms.Controller;


import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/api/lecturer")
public class LecturerController {

    @Inject
    SecurityIdentity identity;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String accessLectureResource(){
        return "I am a lecturer Resource";
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public

}
