package com.fyp.api;

import com.fyp.cli.AuthRole;
import com.fyp.client.FailedToGetAuthRoles;
import com.fyp.client.FailedToGetRoleException;
import com.fyp.db.AuthRoleDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuthRoleServiceTest {
    private AuthRoleDao authRoleDaoMock = Mockito.mock(AuthRoleDao.class);

    private AuthRoleService authRoleService = new AuthRoleService(authRoleDaoMock);

    @Test
    void getRoles_shouldReturnListOfRoles_WhenDaoReturnsRoles() throws SQLException, FailedToGetAuthRoles {
        List<AuthRole> authRoles = new ArrayList<>();
        authRoles.add(new AuthRole(1, "Role1"));
        authRoles.add(new AuthRole(2, "Role2"));

        Mockito.when(authRoleDaoMock.getAuthRoles()).thenReturn(authRoles);

        List<AuthRole> result = authRoleService.getAuthRoles();

        assertEquals(authRoles, result);
    }

    @Test
    void getRoles_ShouldThrowSQLException_WhenDaoThrowsException() throws SQLException {
        Mockito.when(authRoleDaoMock.getAuthRoles()).thenThrow(new SQLException("Test SQL Exception"));

        assertThrows(FailedToGetAuthRoles.class, () -> authRoleService.getAuthRoles());
    }

    @Test
    void getRoleById_ShouldReturnRole_WhenRoleIdIsValid() throws FailedToGetRoleException, SQLException {
        AuthRole authRole = new AuthRole(1, "Role1");

        Mockito.when(authRoleDaoMock.getRoleById(1)).thenReturn(authRole);

        AuthRole result = authRoleService.getRoleById(1);

        assertEquals(authRole, result);
    }

    @Test
    void getRoleById_ShouldThrowSQLException_WhenDaoThrowsException() throws SQLException {
        Mockito.when(authRoleDaoMock.getRoleById(1)).thenThrow(new SQLException("Test SQL Exception"));

        assertThrows(FailedToGetRoleException.class, () -> authRoleService.getRoleById(1));
    }

}
