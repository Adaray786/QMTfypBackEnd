package com.fyp.api;

import com.fyp.auth.TokenService;
import com.fyp.cli.LoginRequest;
import com.fyp.cli.RegisterRequest;
import com.fyp.cli.User;
import com.fyp.client.FailedLoginException;
import com.fyp.client.FailedToGenerateTokenException;
import com.fyp.client.FailedToGetRoleException;
import com.fyp.client.FailedToRegisterException;
import com.fyp.db.AuthDao;
import com.fyp.db.AuthRoleDao;
import org.mindrot.jbcrypt.BCrypt;
import com.fyp.validator.PasswordValidator;

import java.sql.SQLException;

public class AuthService {

    TokenService tokenService;
    PasswordValidator passwordValidator = new PasswordValidator();

    public AuthService(AuthDao authDao, TokenService tokenService) {
        this.authDao = authDao;
        this.tokenService = tokenService;
    }

    private AuthDao authDao;
    private AuthRoleService authRoleService = new AuthRoleService(new AuthRoleDao());




    public String login(LoginRequest login) throws FailedLoginException, SQLException, FailedToGenerateTokenException {
        try {
            User user = authDao.getUserByEmail(login.getEmail());
            if (user == null || !(isValidPassword(login.getPassword(), user.getHashedPassword()))){
                throw new FailedLoginException();
            }

            String token = tokenService.generateToken(user);
            if (token != null) {
                return token;
            }
        } catch (SQLException e) {
            throw new FailedToGenerateTokenException();
        }

        throw new FailedLoginException();
    }

    public boolean isValidPassword(String candidatePassword, String hashedPassword){
        return BCrypt.checkpw(candidatePassword, hashedPassword);
    }


    public void register(RegisterRequest request) throws FailedToRegisterException, SQLException, FailedToGetRoleException {
        String salt = BCrypt.gensalt(9);


        if(!(passwordValidator.validateUser(request).isEmpty())){
            throw new FailedToRegisterException();
        }
        if(authRoleService.getRoleById(request.getRole()) == null){
            throw new FailedToRegisterException();
        }

        String hashedPassword = BCrypt.hashpw(request.getPassword(), salt);
        try {
            authDao.register(request.getUsername(), hashedPassword, request.getRole());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            throw new FailedToRegisterException();
        }
    }


}
