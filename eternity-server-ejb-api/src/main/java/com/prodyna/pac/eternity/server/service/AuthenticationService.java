package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.model.User;

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
     * @param user          the user to be logged in
     * @param plainPassword the users password
     * @return the new created session
     * @throws InvalidPasswordException if the old password is incorrect
     */
    Session login(@NotNull User user, @NotNull String plainPassword)
            throws InvalidPasswordException;

    /**
     * Retrieves a session for a given session id.
     *
     * @param sessionId the id to search for
     * @return the session for the id or null if no session can be found.
     */
    Session getSession(@NotNull String sessionId);

    /**
     * Log out the current connected user.
     *
     * @param sessionId the user's session id to be removed
     */
    void logout(@NotNull String sessionId);

    /**
     * Stores the given password for the user.
     *
     * @param user          the user which gets a new password
     * @param plainPassword the new password to set
     * @return the user with its new password
     */
    User storePassword(@NotNull User user, @NotNull String plainPassword);

    /**
     * Updates the users password
     *
     * @param user             the user which should get a new password
     * @param oldPlainPassword the users old password
     * @param newPlainPassword the users new password
     * @return the User with the updated password
     * @throws InvalidPasswordException if the old password is incorrect
     */
    User changePassword(@NotNull User user, @NotNull String oldPlainPassword, @NotNull String newPlainPassword) throws
            InvalidPasswordException;

}
