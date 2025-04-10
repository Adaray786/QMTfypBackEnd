package com.fyp.resources;

import com.fyp.api.ChallengeService;
import com.fyp.cli.ChallengeRequest;
import com.fyp.cli.ActiveChallenge;
import com.fyp.cli.CompletedChallenge;
import com.fyp.cli.User;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Api("Challenge API")
@Path("/api/challenges")
@Produces(MediaType.APPLICATION_JSON)
public class ChallengeController {
    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @POST
    @Path("/send/{receiverId}/{surahId}")
    @ApiOperation(value = "Send a challenge request", authorizations = {
        @Authorization(value = "Authorization")
    })
    public Response sendChallengeRequest(
        @Auth User sender,
        @PathParam("receiverId") int receiverId,
        @PathParam("surahId") int surahId
    ) {
        try {
            challengeService.sendChallengeRequest(sender.getUserId(), receiverId, surahId);
            return Response.ok().entity("Challenge request sent successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error sending challenge request").build();
        }
    }

    @POST
    @Path("/accept/{requestId}")
    @ApiOperation(value = "Accept a challenge request", authorizations = {
        @Authorization(value = "Authorization")
    })
    public Response acceptChallengeRequest(
        @Auth User user,
        @PathParam("requestId") int requestId
    ) {
        try {
            challengeService.acceptChallengeRequest(requestId);
            return Response.ok().entity("Challenge accepted successfully").build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error accepting challenge").build();
        }
    }

    @POST
    @Path("/reject/{requestId}")
    @ApiOperation(value = "Reject a challenge request", authorizations = {
        @Authorization(value = "Authorization")
    })
    public Response rejectChallengeRequest(
        @Auth User user,
        @PathParam("requestId") int requestId
    ) {
        try {
            challengeService.rejectChallengeRequest(requestId);
            return Response.ok().entity("Challenge rejected successfully").build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error rejecting challenge").build();
        }
    }

    @GET
    @Path("/requests")
    @ApiOperation(value = "Get pending challenge requests", authorizations = {
        @Authorization(value = "Authorization")
    })
    public Response getChallengeRequests(@Auth User user) {
        try {
            List<ChallengeRequest> requests = challengeService.getChallengeRequests(user.getUserId());
            return Response.ok(requests).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching challenge requests").build();
        }
    }

    @GET
    @Path("/active")
    @ApiOperation(value = "Get active challenges", authorizations = {
        @Authorization(value = "Authorization")
    })
    public Response getActiveChallenges(@Auth User user) {
        try {
            List<ActiveChallenge> challenges = challengeService.getActiveChallenges(user.getUserId());
            return Response.ok(challenges).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching active challenges").build();
        }
    }

    @GET
    @Path("/completed")
    @ApiOperation(value = "Get completed challenges", authorizations = {
        @Authorization(value = "Authorization")
    })
    public Response getCompletedChallenges(@Auth User user) {
        try {
            List<CompletedChallenge> challenges = challengeService.getCompletedChallenges(user.getUserId());
            return Response.ok(challenges).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching completed challenges").build();
        }
    }

    @POST
    @Path("/win/{challengeId}")
    @ApiOperation(value = "Mark a challenge as won by the current user", authorizations = {
        @Authorization(value = "Authorization")
    })
    public Response winChallenge(
        @Auth User user,
        @PathParam("challengeId") int challengeId
    ) {
        try {
            challengeService.winChallenge(challengeId, user.getUserId());
            return Response.ok().entity("Challenge marked as won successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error marking challenge as won").build();
        }
    }

    @GET
    @Path("/active/{surahId}")
    @ApiOperation(value = "Get active challenge for a specific surah", authorizations = {
        @Authorization(value = "Authorization")
    })
    public Response getActiveChallengeBySurah(
        @Auth User user,
        @PathParam("surahId") int surahId
    ) {
        try {
            ActiveChallenge challenge = challengeService.getActiveChallengeBySurahAndUser(surahId, user.getUserId());
            if (challenge != null) {
                return Response.ok(challenge).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("No active challenge found for this surah").build();
            }
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching active challenge").build();
        }
    }
} 