package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.model.Login;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

/**
 * Authentication service provides service for operating with passwords, logins ans sessions.
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
    Login login(@NotNull Login login) throws InvalidLoginException;

    /**
     * Log out the current connected user.
     *
     * @param sessionId the user's session id to be removed
     */
    void logout(@NotNull String sessionId);

}
