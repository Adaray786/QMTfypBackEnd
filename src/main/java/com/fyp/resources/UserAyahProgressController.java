package com.fyp.resources;

import com.fyp.client.FailedToInsertAyahProgress;
import io.swagger.annotations.Api;
import com.fyp.api.UserAyahProgressService;
import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToGetAyahProgress;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Api("User Ayah Progress API")
@Path("/api/ayahProgress")
public class UserAyahProgressController {

    private final UserAyahProgressService userAyahProgressService;

    // Constructor
    public UserAyahProgressController(UserAyahProgressService userAyahProgressService) {
        this.userAyahProgressService = userAyahProgressService;
    }

    // Endpoint to upsert Ayah progress
    @POST
    @Path("/upsert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upsertAyahProgress(UserAyahProgress userAyahProgress) {
        try {
            userAyahProgressService.upsertUserAyahProgress(
                    userAyahProgress.getUserId(),
                    userAyahProgress.getAyahId(),
                    userAyahProgress.isMemorized()
            );
            return Response.ok("Ayah progress updated successfully").build();
        } catch (FailedToInsertAyahProgress e) {
            return Response.serverError().entity("Failed to update Ayah progress").build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint to get Ayah progress by user and Ayah
    @GET
    @Path("/{userId}/{ayahId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAyahProgress(@PathParam("userId") int userId, @PathParam("ayahId") int ayahId) {
        try {
            UserAyahProgress progress = userAyahProgressService.getUserAyahProgress(userId, ayahId);
            return progress != null
                    ? Response.ok(progress).build()
                    : Response.status(Response.Status.NOT_FOUND).entity("Ayah progress not found").build();
        } catch (FailedToGetAyahProgress e) {
            return Response.serverError().entity("Failed to retrieve Ayah progress").build();
        }
    }

    // Endpoint to get all Ayah progress for a user
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAyahProgressByUser(@PathParam("userId") int userId) {
        try {
            List<UserAyahProgress> progressList = userAyahProgressService.getAllAyahProgressByUser(userId);
            return Response.ok(progressList).build();
        } catch (FailedToGetAyahProgress e) {
            return Response.serverError().entity("Failed to retrieve Ayah progress").build();
        }
    }
}
