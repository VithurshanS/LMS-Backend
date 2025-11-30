package org.lms.Controller;


import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/")
public class user{

    @Inject
    SecurityIdentity identity;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String accessStudentResource(){
        return "I am a home Resource";
    }

}
