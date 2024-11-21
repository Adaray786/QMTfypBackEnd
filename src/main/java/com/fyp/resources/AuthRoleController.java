package com.fyp.resources;

import io.swagger.annotations.Api;
import com.fyp.api.AuthRoleService;
import com.fyp.cli.AuthRole;
import com.fyp.client.FailedToGetAuthRoles;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Api("Authentication API")
@Path("/api")
public class AuthRoleController {
    public AuthRoleController(AuthRoleService authRoleService) {
        this.authRoleService = authRoleService;
    }

    AuthRoleService authRoleService;
    @GET
    @Path("/authRoles")
    @Produces(MediaType.APPLICATION_JSON)

    public Response getRoles(){
        try{
            List<AuthRole> authRoles = authRoleService.getAuthRoles();

            return Response.ok(authRoles).build();
        } catch (FailedToGetAuthRoles e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }catch(SQLException e) {
            return  Response.serverError().build();
        }


    }


}
