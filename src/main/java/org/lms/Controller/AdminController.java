package org.lms.Controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.*;
import org.lms.Model.Department;
import org.lms.Service.*;

import java.util.List;
import java.util.UUID;


class DeptCreateRequest{
    public String name;
}

class LectAssignRequest{
    public UUID moduleId;
    public UUID lecturerId;
}

class ApproveRequest {
    public String userId;
}

class ControlRequest {
    public String userId;
    public String control;
}
class ModuleCreateRequest {
    public String code;
    public String name;
    public int limit;
    public UUID departmentId;
    public UUID adminId;
}

@Path("/api/admin")
public class AdminController {
    @Inject
    UserService userService;

    @Inject
    LecturerService lecturerService;

    @Inject
    StudentService studentService;

    @Inject
    ModuleService moduleService;

    @Inject
    DepartmentService departmentService;


    //############################### DEPARTMENT###################################################
    @POST
    @Path("/create-dept") //admin
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDept(DeptCreateRequest req){
        try{
            Department dept = departmentService.createDepartment(req.name);
            return Response.status(201).entity(dept).build();

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

    //###########################################  LECTURER #######################

    @PATCH
    @Path("/approve-lecturer") //admin
    @Consumes(MediaType.APPLICATION_JSON)
    public Response approve(ApproveRequest request){
        return userService.approveUser(request.userId);
    }

    @GET
    @Path("/all-lecturers") //admin
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLecturers() {
        try {
            List<UserResponseDto> lecturers = lecturerService.getAllLecturers2();
            return Response.ok(lecturers).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error fetching lecturers: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/lecturer/{id}") //lecturer +admin
    public Response getLecturerById(@PathParam("id") UUID lecturerId) {
        try {
            UserResponseDto dto = lecturerService.getLecturerDetails2(lecturerId);
            if (dto == null) {
                return Response.status(404).entity("Lecturer not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching lecturer: " + e.getMessage()).build();
        }
    }
//##################################################### STUDENT ########################################################
    @GET
    @Path("/all-students") //admin + lecturers
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudents() {
        try {
            List<UserResponseDto> students = studentService.getAllStudents2();
            return Response.ok(students).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error fetching students: " + e.getMessage()).build();
        }
    }




    @GET
    @Path("/student/{id}") //lecturer + admin +student
    public Response getStudentById(@PathParam("id") UUID studentId) {
        try {
            UserResponseDto dto = studentService.getStudentDetails2(studentId);
            if (dto == null) {
                return Response.status(404).entity("Student not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching student: " + e.getMessage()).build();
        }
    }

    //#################################################################  ADMIN ########################

    @PATCH
    @Path("/control-user") //admin
    @Consumes(MediaType.APPLICATION_JSON)
    public Response controllUser(ControlRequest cr){
        try{
            userService.controllUserAccess(cr.userId, cr.control);
            return Response.ok("user modified").build();
        }catch (Exception e){
            return Response.notModified(e.getMessage()).build();
        }

    }

    //##################################################### MODULE ###########################################################

    @POST
    @Path("/create-module") //admin
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createModule(ModuleCreateRequest request){
        try {
            moduleService.createModule(request.code, request.name, request.limit, request.departmentId, request.adminId);
            return Response.status(201).build();
        } catch (Exception e) {
            return Response.status(401).build();
        }

    }

    @GET
    @Path("/getmodule-dept/{dept_id}") //admin + student + lecturer
    public Response getModulesbyDeptId(@PathParam("dept_id") UUID deptId){
        try{
            List<ModuleDetailDto> modules = moduleService.getModulesByDeptId(deptId);
            return Response.ok(modules).build();
        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

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

    @PATCH
    @Path("/assign-lecturer")
    public Response assignlecturer(LectAssignRequest req){ //admin
        try {
            moduleService.assignLecturer(req.moduleId,req.lecturerId);
            return Response.ok().build();

        }catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }

    }

    @GET
    @Path("/studentlist-from-module/{id}") //lecturer +admin +student
    public Response getStudentsByModuleId(@PathParam("id") UUID moduleId) {
        try {
            List<UserResponseDto> dto = studentService.getStudentsByModuleId(moduleId);
            if (dto == null) {
                return Response.status(404).entity("Lecturer not found").build();
            }
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error fetching lecturer: " + e.getMessage()).build();
        }
    }

    //#################################################### TEST ##############################################

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
