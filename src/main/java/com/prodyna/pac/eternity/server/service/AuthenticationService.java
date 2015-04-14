package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Authentication service provides service for operating with passwords and logins.
 */
@Local
public interface AuthenticationService {

    /**
     * Tries to login the user with the given password.
     *
     * @param user          the user to be logged in
     * @param plainPassword the users password
     */
    void login(@NotNull User user, @NotNull String plainPassword);

    /**
     * Log out the current connected user.
     */
    void logout();

    /**
     * Stores the given password for the user.
     *
     * @param user          the user which gets a new password
     * @param plainPassword the new password to set
     * @return
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
    User changePassword(@NotNull User user, @NotNull String oldPlainPassword, @NotNull String newPlainPassword) throws InvalidPasswordException;

}
