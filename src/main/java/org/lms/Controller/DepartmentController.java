package org.lms.Controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.DeptCreateRequest;
import org.lms.Service.DepartmentService;

import java.util.UUID;

@Path("/api/department")
public class DepartmentController {

    @Inject
    DepartmentService departmentService;


    @RolesAllowed("admin")
    @POST
    @Path("/create") //admin
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDept(DeptCreateRequest req){
        try{

            return Response.status(201).entity(departmentService.createDepartment(req.name)).build();

        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }

    }

    @PermitAll
    @GET
    @Path("/all") //admin
    public Response getAllDept(){
        try{
            return Response.ok(departmentService.getAllDepartments()).build();
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }


    @RolesAllowed({"admin","student","lecturer"})
    @GET
    @Path("/id/{id}")
    public Response getDepartmentbyId(@PathParam("id") UUID departmentId){
        try{
            return Response.ok(departmentService.getDepartmentById(departmentId)).build();
        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
}
