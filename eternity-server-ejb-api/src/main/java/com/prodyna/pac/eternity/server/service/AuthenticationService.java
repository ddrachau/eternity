package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.model.Session;

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
     * @param userIdentifier the user to be logged in
     * @param plainPassword  the users password
     * @return the new created session
     * @throws InvalidLoginException if the password or user is incorrect
     */
    Session login(@NotNull String userIdentifier, @NotNull String plainPassword)
            throws InvalidLoginException;

    /**
     * Log out the current connected user.
     *
     * @param sessionId the user's session id to be removed
     */
    void logout(@NotNull String sessionId);

}
