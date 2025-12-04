package org.lms.Controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.LoginRequestDto;
import org.lms.Dto.RegistrationRequestDto;
import org.keycloak.admin.client.Keycloak;
import org.lms.Service.UserService;

@Path("/auth")
public class AuthendicationController {

    @Inject
    Keycloak keycloak;

    @Inject
    UserService userService;

    @GET
    @Path("/sample")
    @Produces(MediaType.APPLICATION_JSON)
    public Response see(){
        return Response.ok("test").build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegistrationRequestDto userDto) {
        return userService.registerUser(userDto,"ironone");
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDto credentials){
        return userService.loginUser(credentials);
    }
}