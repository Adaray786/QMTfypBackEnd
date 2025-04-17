package com.fyp.resources;


import com.fyp.api.AuthRoleService;
import com.fyp.cli.AuthRole;
import com.fyp.client.FailedToGetAuthRoles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthRoleControllerTest {
    AuthRoleService authRoleServiceMock = Mockito.mock(AuthRoleService.class);

    AuthRoleController authRoleController = new AuthRoleController(authRoleServiceMock);

    @Test
    void getRolesSuccess_ShouldReturn200() throws FailedToGetAuthRoles, SQLException {
        List<AuthRole> authRoles = new ArrayList<>();
        authRoles.add(new AuthRole(1, "Role1"));
        authRoles.add(new AuthRole(2, "Role2"));

        when(authRoleServiceMock.getAuthRoles()).thenReturn(authRoles);
        Response response = authRoleController.getRoles();

        assertEquals(200, response.getStatus());
    }

    @Test
    void getRolesFailure_ShouldReturn401() throws FailedToGetAuthRoles, SQLException {

        Mockito.when(authRoleServiceMock.getAuthRoles()).thenThrow(FailedToGetAuthRoles.class);
        Response response = authRoleController.getRoles();

        assertEquals(400, response.getStatus());
    }

    @Test
    void getRolesFailure_ShouldReturn500() throws SQLException, FailedToGetAuthRoles {
        Mockito.when(authRoleServiceMock.getAuthRoles()).thenThrow(SQLException.class);
        Response response = authRoleController.getRoles();

        assertEquals(500, response.getStatus());
    }

}
