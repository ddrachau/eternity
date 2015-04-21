package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
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
     * Retrieves a session for a given session id.
     *
     * @param sessionId the id to search for
     * @return the session for the id or null if no session can be found.
     */
    Session getSession(String sessionId);

    /**
     * Log out the current connected user.
     *
     * @param sessionId the user's session id to be removed
     */
    void logout(@NotNull String sessionId);

    /**
     * Stores the given password for the user.
     *
     * @param userIdentifier the user which gets a new password
     * @param plainPassword  the new password to set
     */
    void storePassword(@NotNull String userIdentifier, @NotNull String plainPassword) throws InvalidUserException;

    /**
     * Updates the users password
     *
     * @param userIdentifier   the user which should get a new password
     * @param oldPlainPassword the users old password
     * @param newPlainPassword the users new password
     * @throws InvalidUserException if the user is incorrect
     * @throws InvalidUserException if the password is incorrect
     */
    void changePassword(@NotNull String userIdentifier, @NotNull String oldPlainPassword, @NotNull String newPlainPassword) throws
            InvalidUserException, InvalidPasswordException;

}
