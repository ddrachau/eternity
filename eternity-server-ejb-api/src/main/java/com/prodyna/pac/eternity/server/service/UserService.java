package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.exception.technical.ElementAlreadyExistsRuntimeException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.model.Booking;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Basic service for operating on users.
 */
@Local
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created project with the generated id
     */
    User create(@NotNull User user);

    /**
     * Searches for a single user.
     *
     * @param identifier the identifier to search for
     * @return the found user or null if none was found
     */
    User get(@NotNull String identifier);

    /**
     * Searches for a single user.
     *
     * @param booking the booking the user is mapped to
     * @return the found user
     */
    User getByBooking(@NotNull Booking booking);

    /**
     * Searches for a single user.
     *
     * @param sessionId the session the user is mapped to
     * @return the found user
     */
    User getBySessionId(@NotNull String sessionId);

    /**
     * Searches for all users.
     *
     * @return a list of all found users.
     */
    List<User> findAll();

    /**
     * Updates the given user in the database.
     *
     * @param user the project to be updated
     * @return the updated user
     * @throws NoSuchElementRuntimeException        if the element to be updated cannot be found
     * @throws ElementAlreadyExistsRuntimeException if a user with the same identifier already exists
     */
    User update(@NotNull User user) throws NoSuchElementRuntimeException, ElementAlreadyExistsRuntimeException;

    /**
     * Removes the given user from the database.
     *
     * @param identifier the user to be deleted
     * @throws NoSuchElementRuntimeException if the given user cannot be found
     */
    void delete(@NotNull String identifier) throws NoSuchElementRuntimeException;

    /**
     * Assigns the given project to the given user.
     *
     * @param user    the user to be assigned
     * @param project the target project
     * @throws NoSuchElementRuntimeException if the given user or project cannot be found
     */
    void assignUserToProject(User user, Project project) throws NoSuchElementRuntimeException;

    /**
     * Unassign the given project from the given user.
     *
     * @param user    the user to be unassigned
     * @param project the target project
     * @throws NoSuchElementRuntimeException if the given user or project cannot be found
     */
    void unassignUserFromProject(User user, Project project) throws NoSuchElementRuntimeException;

    /**
     * Search for all the users assigned to the given project.
     *
     * @param project the target project
     * @return users which are assigned to the project
     */
    List<User> findAllAssignedToProject(Project project);

    /**
     * Checks if the given user is assigned to the given project
     *
     * @param user    the user to check
     * @param project the project to check
     * @return true if the user is assigned to the project, false otherwise
     */
    boolean isAssignedTo(@NotNull User user, @NotNull Project project);


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

    /**
     * Checks if the given password checks against the given user.
     *
     * @param userIdentifier the user to check against
     * @param plainPassword  the users password to check
     * @throws NoSuchElementRuntimeException if the user does not exists
     * @throws InvalidPasswordException      if the old password is incorrect
     */
    void checkIfPasswordIsValid(@NotNull String userIdentifier, @NotNull String plainPassword)
            throws InvalidPasswordException, InvalidUserException;

}
