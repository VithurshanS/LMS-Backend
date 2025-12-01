package org.lms.Controller;


import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lms.Model.Enrollment;
import org.lms.Service.EnrollmentService;

import java.util.UUID;


@Path("/api/student")
public class StudentController {

    @Inject
    SecurityIdentity identity;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String accessStudentResource(){
        return "I am a student Resource";
    }

    @Inject
    EnrollmentService enrollmentService;

    @POST
    @Path("/enroll")
    public Response enrollStudent(@QueryParam("studentId") UUID studentId,
                                  @QueryParam("moduleId") UUID moduleId) {
        try {
            Enrollment enr = enrollmentService.enrollStudent(studentId, moduleId);
            return Response.status(201).entity(enr).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(400).entity(e.getMessage()).build(); // "Module full" or "Already enrolled"
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

}
