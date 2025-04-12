package com.fyp.resources;

import com.fyp.cli.User;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import com.fyp.api.UserSurahProgressService;
import com.fyp.cli.UserSurahProgress;
import com.fyp.client.FailedToGetSurahProgress;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Api("User Surah Progress API")
@Path("/api/surahProgress")
public class UserSurahProgressController {

    private final UserSurahProgressService userSurahProgressService;

    // Constructor
    public UserSurahProgressController(UserSurahProgressService userSurahProgressService) {
        this.userSurahProgressService = userSurahProgressService;
    }

    // Endpoint to upsert Surah progress
    @POST
    @Path("/upsert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update surah progress", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response upsertSurahProgress(
            @Auth User user,
            UserSurahProgress userSurahProgress) {
        try {
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authentication required").build();
            }

            if (user.getUserId() != userSurahProgress.getUserId()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("You are not authorized to access this user's Surah progress").build();
            }

            userSurahProgressService.createOrUpdateSurahProgress(
                    userSurahProgress.getUserId(),
                    userSurahProgress.getSurahId()
            );
            return Response.ok("Surah progress updated successfully").build();
        } catch (FailedToGetSurahProgress e) {
            return Response.serverError().entity("Failed to update Surah progress").build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint to get Surah progress by user and Surah
    @GET
    @Path("/{userId}/{surahId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get list of friends", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getSurahProgress(
            @Auth User user,
            @PathParam("userId") int userId,
            @PathParam("surahId") int surahId) {
        try {
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authentication required").build();
            }

            if (user.getUserId() != userId) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("You are not authorized to access this user's Surah progress").build();
            }
            UserSurahProgress progress = userSurahProgressService.getUserSurahProgress(userId, surahId);
            return progress != null
                    ? Response.ok(progress).build()
                    : Response.status(Response.Status.NOT_FOUND).entity("Surah progress not found").build();
        } catch (FailedToGetSurahProgress e) {
            return Response.serverError().entity("Failed to retrieve Surah progress").build();
        }
    }

    // Endpoint to get all Surah progress for a user
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get list of friends", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getAllSurahProgressByUser(
            @Auth User user,
            @PathParam("userId") int userId) {
        try {
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authentication required").build();
            }

            if (user.getUserId() != userId) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("You are not authorized to access this user's Surah progress").build();
            }
            List<UserSurahProgress> progressList = userSurahProgressService.getAllSurahProgressByUser(userId);
            return Response.ok(progressList).build();
        } catch (FailedToGetSurahProgress e) {
            return Response.serverError().entity("Failed to retrieve Surah progress").build();
        }
    }
}
