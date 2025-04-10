package com.fyp.resources;

import com.fyp.api.UserService;
import com.fyp.cli.User;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ApiParam;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Api("User API")
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/search")
    @PermitAll
    public Response searchUsers(@Auth User user, @QueryParam("query") String query) {
        try {
            List<User> users = userService.searchUsers(query, user.getUserId());
            System.out.printf(users.toString());
            return Response.ok(users).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error searching users.").build();
        }
    }

    @GET
    @Path("/{userId}")
    @ApiOperation(value = "Get user details by ID", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getUserById(
            @Auth User user,
            @PathParam("userId") int userId
    ) {
        try {
            User userDetails = userService.getUserById(userId);
            if (userDetails == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found").build();
            }
            return Response.ok().entity(userDetails).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error retrieving user details.").build();
        }
    }
}
