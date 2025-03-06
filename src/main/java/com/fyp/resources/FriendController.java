package com.fyp.resources;

import com.fyp.api.FriendService;
import com.fyp.cli.Friend;
import com.fyp.cli.FriendRequest;
import com.fyp.cli.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Api("Friends API")
@Path("/api/friends")
@Produces(MediaType.APPLICATION_JSON)
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    // ✅ Send a Friend Request (Authenticated User as Sender)
    @POST
    @Path("/{receiverId}/send-request")
    @PermitAll
    @ApiOperation(value = "Send a friend request to another user", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response sendFriendRequest(@Auth User sender, @PathParam("receiverId") int receiverId) {
        try {
            friendService.sendFriendRequest(sender.getUserId(), receiverId);
            return Response.ok().entity("Friend request sent to user " + receiverId).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error sending friend request.").build();
        }
    }

    // ✅ Accept a Friend Request (Authenticated User Accepts)
    @POST
    @Path("/{requestId}/accept")
    @ApiOperation(value = "Accept a friend request", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response acceptFriendRequest(@Auth User user, @PathParam("requestId") int requestId) {
        try {
            friendService.acceptFriendRequest(requestId);
            return Response.ok().entity("Friend request accepted.").build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error accepting friend request.").build();
        }
    }

    // ✅ Reject a Friend Request (Authenticated User Rejects)
    @POST
    @Path("/{requestId}/reject")
    @ApiOperation(value = "Reject a friend request", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response rejectFriendRequest(@Auth User user, @PathParam("requestId") int requestId) {
        try {
            friendService.rejectFriendRequest(requestId);
            return Response.ok().entity("Friend request rejected.").build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error rejecting friend request.").build();
        }
    }

    // ✅ Remove a Friend (Authenticated User Removes)
    @DELETE
    @Path("/{friendId}/remove")
    @ApiOperation(value = "Remove a friend", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response removeFriend(@Auth User user, @PathParam("friendId") int friendId) {
        try {
            friendService.removeFriend(user.getUserId(), friendId);
            return Response.ok().entity("Friend removed successfully.").build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error removing friend.").build();
        }
    }

    // ✅ Get Pending Friend Requests (Authenticated User)
    @GET
    @Path("/requests")
    @ApiOperation(value = "Get pending friend requests", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getFriendRequests(@Auth User user) {
        try {
            List<FriendRequest> requests = friendService.getFriendRequests(user.getUserId());
            return Response.ok(requests).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching friend requests.").build();
        }
    }

    // ✅ Get Friends List (Authenticated User)
    @GET
    @Path("/")
    @ApiOperation(value = "Get list of friends", authorizations = {
            @Authorization(value = "Authorization")
    })
    public Response getFriends(@Auth User user) {
        try {
            List<Friend> friends = friendService.getFriends(user.getUserId());
            return Response.ok(friends).build();
        } catch (SQLException e) {
            return Response.serverError().entity("Error fetching friends.").build();
        }
    }
}
