package org.lms.Controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.LoginRequestDto;
import org.lms.Dto.RegistrationRequestDto;
import org.keycloak.admin.client.Keycloak;
import org.lms.Service.DepartmentService;
import org.lms.Service.UserService;

@Path("/auth")
public class UserController {

    @Inject
    Keycloak keycloak;

    @Inject
    UserService userService;

    @Inject
    DepartmentService departmentService;

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
        if (userDto != null && userDto.role != null) {
            userDto.role = userDto.role.toLowerCase();
        }
        return userService.registerUser(userDto,"ironone");
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDto credentials){
        return userService.loginUser(credentials);
    }


    @GET
    @Path("/getuser")
    public Response getLoginedUser(){
        try{
            return Response.ok(userService.getProfileFromToken()).build();
        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }

    }

    @GET
    @Path("/get-all-dept") //admin
    public Response getAllDept(){
        try{
            return Response.ok(departmentService.getAllDepartments()).build();
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }


}