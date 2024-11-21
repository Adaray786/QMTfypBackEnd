package com.fyp.api;

import com.fyp.cli.AuthRole;
import com.fyp.client.FailedToGetAuthRoles;
import com.fyp.client.FailedToGetRoleException;
import com.fyp.db.AuthRoleDao;

import java.sql.SQLException;
import java.util.List;

public class AuthRoleService {
    private final AuthRoleDao authRoleDao;

    public AuthRoleService(AuthRoleDao authRoleDao) {
        this.authRoleDao = authRoleDao;
    }

    public List<AuthRole> getAuthRoles() throws FailedToGetAuthRoles, SQLException {
        try{
            return authRoleDao.getAuthRoles();
        }catch (SQLException e) {
            System.err.println(e.getMessage());

            throw new FailedToGetAuthRoles();
        }
    }

    public AuthRole getRoleById(int roleId) throws FailedToGetRoleException {
        try{
            return authRoleDao.getRoleById(roleId);
        }catch(SQLException e){
            System.err.println(e.getMessage());

            throw new FailedToGetRoleException();
        }
    }


}
