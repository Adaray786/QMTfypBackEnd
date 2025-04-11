package com.fyp.resources;

import com.fyp.api.RecommendationService;
import com.fyp.cli.SurahRecommendation;
import com.fyp.cli.User;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api("Recommendation API")
@Path("/api/recommendations")
@Produces(MediaType.APPLICATION_JSON)
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GET
    @Path("/revision")
    @ApiOperation(value = "Get revision recommendations", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getRevisionRecommendations(@Auth User user) {
        try {
            List<SurahRecommendation> recommendations = recommendationService.getRevisionRecommendations(user.getUserId());
            return Response.ok(recommendations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting revision recommendations: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/new")
    @ApiOperation(value = "Get new surah recommendations", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getNewSurahRecommendations(@Auth User user) {
        try {
            List<SurahRecommendation> recommendations = recommendationService.getNewSurahRecommendations(user.getUserId());
            return Response.ok(recommendations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting new surah recommendations: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/{userId}/mark-revised/{surahId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Mark a surah as revised", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response markAsRevised(
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
                        .entity("You are not authorized to update this user's progress").build();
            }

            recommendationService.markAsRevised(userId, surahId);
            return Response.ok("Surah marked as revised successfully").build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("Failed to mark surah as revised: " + e.getMessage())
                    .build();
        }
    }
} 