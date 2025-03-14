package com.fyp.resources;

import com.fyp.api.UserScoreService;
import com.fyp.cli.User;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

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
        } catch (SQLException e) {
            return Response.serverError().entity("Error retrieving user score.").build();
        }
    }
}
