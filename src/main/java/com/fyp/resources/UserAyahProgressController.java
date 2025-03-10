package com.fyp.resources;

import com.fyp.cli.User;
import com.fyp.client.FailedToCheckAyahProgressException;
import com.fyp.client.FailedToInsertAyahProgress;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import com.fyp.api.UserAyahProgressService;
import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToGetAyahProgress;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

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

    @POST
    @Path("/upsert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Upsert Ayah Progress", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response upsertAyahProgress(
            @Auth User authenticatedUser, // Authenticated user
            UserAyahProgress userAyahProgress) {
        try {
            // ✅ Authorization Check: Ensure the authenticated user matches the userId in the request
            if (authenticatedUser.getUserId() != userAyahProgress.getUserId()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("You are not authorized to update this user's Ayah progress").build();
            }

            // ✅ Update Ayah Progress
            userAyahProgressService.upsertUserAyahProgress(
                    userAyahProgress.getUserId(),
                    userAyahProgress.getAyahId(),
                    userAyahProgress.isMemorized()
            );
            return Response.ok("Ayah progress updated successfully").build();

        } catch (FailedToInsertAyahProgress e) {
            return Response.serverError().entity("Failed to update Ayah progress").build();
        } catch (SQLException | FailedToCheckAyahProgressException e) {
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

    @GET
    @Path("/{userId}/surah/{surahId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get Ayah Progress by Surah and User", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getAyahProgressBySurahAndUser(
            @Auth User authenticatedUser, // Authenticated user
            @PathParam("userId") int userId,
            @PathParam("surahId") int surahId) {
        try {
            // ✅ Check if user is authenticated
            if (authenticatedUser == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authentication required").build();
            }

            // ✅ Check if the user is authorized to access this progress
            if (authenticatedUser.getUserId() != userId) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("You are not authorized to access this user's Surah progress").build();
            }

            // ✅ Fetch User's Ayah Progress
            List<UserAyahProgress> progressList = userAyahProgressService.getAyahProgressBySurahAndUser(userId, surahId);
            return Response.ok(progressList).build();

        } catch (SQLException e) {
            return Response.serverError().entity("Failed to fetch Ayah progress for the given User and Surah").build();
        } catch (FailedToGetAyahProgress | FailedToCheckAyahProgressException e) {
            throw new RuntimeException(e);
        }
    }

}
