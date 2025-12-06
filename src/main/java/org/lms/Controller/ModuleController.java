package org.lms.Controller;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.LectAssignRequest;
import org.lms.Dto.ModuleCreateRequest;
import org.lms.Dto.ModuleDetailDto;
import org.lms.Service.ModuleService;

import java.util.List;
import java.util.UUID;

@Path("/api/module")
public class ModuleController {

    @Inject
    ModuleService moduleService;
    @RolesAllowed({"admin","lecturer"})
    @GET
    @Path("/lecturerId/{id}") //admin + lecturer
    public Response getModulesbyLectId(@PathParam("id") UUID lectId){
        try{
            List<ModuleDetailDto> modules = moduleService.getModulesByLectId(lectId);
            return Response.ok(modules).build();
        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @RolesAllowed({"admin","lecturer","student"})
    @GET
    @Path("/studentId/{id}") //lecturer +admin +student
    public Response getLecturerById(@PathParam("id") UUID studentId) {
        try {
            List<ModuleDetailDto> dto = moduleService.getModulesByStudentId(studentId);
            if (dto == null) {
                return Response.status(404).entity("Lecturer not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching lecturer: " + e.getMessage()).build();
        }
    }
    @RolesAllowed({"admin","lecturer","student"})
    @GET
    @Path("/departmentId/{dept_id}") //admin + student + lecturer
    public Response getModulesbyDeptId(@PathParam("dept_id") UUID deptId){
        try{
            List<ModuleDetailDto> modules = moduleService.getModulesByDeptId(deptId);
            return Response.ok(modules).build();
        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
    @RolesAllowed("admin")
    @POST
    @Path("/create") //admin
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createModule(ModuleCreateRequest request){
        try {

            return Response.status(201).entity(moduleService.createModule(request.code, request.name, request.limit, request.departmentId, request.adminId)).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }

    }




    @RolesAllowed("admin")
    @PATCH
    @Path("/assignLecturer")
    public Response assignlecturer(LectAssignRequest req){ //admin
        try {
            moduleService.assignLecturer(req.moduleId,req.lecturerId);
            return Response.ok().build();

        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }

    }
}
