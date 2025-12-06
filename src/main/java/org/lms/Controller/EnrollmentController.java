package org.lms.Controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.lms.Dto.EnrollRequest;
import org.lms.Dto.UnenrollRequest;
import org.lms.Model.Enrollment;
import org.lms.Service.EnrollmentService;

@Path("/api/enrollment")
public class EnrollmentController {

    @Inject
    EnrollmentService enrollmentService;

    @RolesAllowed("student")
    @POST
    @Path("/enroll")
    public Response enrollStudent(EnrollRequest req) {
        try {
            Enrollment enr = enrollmentService.enrollStudent(req.studentId, req.moduleId);
            return Response.status(201).entity(enr).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @RolesAllowed("admin")
    @DELETE
    @Path("/unenroll")
    public Response unenrollStudent(UnenrollRequest ur){
        try{
            enrollmentService.unenrollStudent(ur.studentId,ur.moduleId);
            return Response.ok().build();
        }catch (Exception e){
            return Response.status(400).build();
        }
    }
}
