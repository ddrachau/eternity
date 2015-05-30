package com.prodyna.pac.eternity.authentication.service;

import com.prodyna.pac.eternity.common.model.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.common.model.authentication.Login;

import javax.ejb.Local;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Authentication service provides services for operating with passwords, logins ans sessions.
 */
@Local
public interface AuthenticationService {

    /**
     * Tries to login the user with the given password.
     *
     * @param login the login data to login with
     * @return the new created login
     * @throws InvalidLoginException if the password or user is incorrect
     */
    Login login(@NotNull @Valid Login login) throws InvalidLoginException;

    /**
     * Tries to login with a rememberMe token.
     *
     * @param rememberMeToken the login data to login with
     * @return the new created login
     * @throws InvalidLoginException if the token cannot be used to login
     */
    Login login(@NotNull String rememberMeToken) throws InvalidLoginException;

    /**
     * Log out the current connected user.
     *
     * @param sessionId  the user's session id to be removed
     * @param rememberMe optional remember me to be logged out as well
     */
    void logout(@NotNull String sessionId, String rememberMe);

}
