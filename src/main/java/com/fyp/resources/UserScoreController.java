package com.fyp.resources;

import com.fyp.api.UserScoreService;
import com.fyp.cli.User;
import com.fyp.client.FailedToGetFriendsScoresException;
import com.fyp.client.FailedToGetUserScoreException;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Map;

@Api("User Scores API")
@Path("/api/scores")
@Produces(MediaType.APPLICATION_JSON)
public class UserScoreController {

    private final UserScoreService userScoreService;

    public UserScoreController(UserScoreService userScoreService) {
        this.userScoreService = userScoreService;
    }

    // ✅ Retrieve User's Score
    @GET
    @Path("/userScore")
    @ApiOperation(value = "Get user score", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getUserScore(@Auth User user) {
        try {
            int score = userScoreService.getUserScore(user.getUserId());
            System.out.printf(String.valueOf(score));
            return Response.ok().entity(score).build();
        } catch (FailedToGetUserScoreException e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ Retrieve Friends' Scores
    @GET
    @Path("/friends")
    @ApiOperation(value = "Get friends' scores", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getFriendsScores(@Auth User user) {
        try {
            Map<Integer, Integer> friendsScores = userScoreService.getFriendsScores(user.getUserId());
            return Response.ok().entity(friendsScores).build();
        } catch (FailedToGetFriendsScoresException e) {
            return Response.serverError().entity("Error retrieving friends' scores.").build();
        }
    }
}

