package com.prodyna.pac.eternity.user.service;

import com.prodyna.pac.eternity.common.model.FilterRequest;
import com.prodyna.pac.eternity.common.model.FilterResponse;
import com.prodyna.pac.eternity.common.model.booking.Booking;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.common.model.project.Project;
import com.prodyna.pac.eternity.common.model.user.User;

import javax.ejb.Local;
import javax.validation.Valid;
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
     * @throws ElementAlreadyExistsException if an user with the same identifier already exists
     */
    User create(@NotNull @Valid User user) throws ElementAlreadyExistsException;

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
     * Searches for a single user.
     *
     * @param rememberMeId the rememberMe the user is mapped to
     * @return the found user
     */
    User getByRememberMe(@NotNull String rememberMeId);

    /**
     * Searches for all users.
     *
     * @return a list of all found users.
     */
    List<User> find();

    /**
     * Search for all the user matching the filter.
     *
     * @param filterRequest the filter parameter for this search
     * @return user which match the filter.
     */
    FilterResponse<User> find(@NotNull FilterRequest filterRequest);

    /**
     * Updates the given user in the database.
     *
     * @param user the project to be updated
     * @return the updated user
     * @throws ElementAlreadyExistsException if a user with the same identifier already exists
     */
    User update(@NotNull @Valid User user) throws ElementAlreadyExistsException;

    /**
     * Removes the given user from the database.
     *
     * @param identifier the user to be deleted
     */
    void delete(@NotNull String identifier);

    /**
     * Assigns the given project to the given user.
     *
     * @param user    the user to be assigned
     * @param project the target project
     */
    void assignUserToProject(User user, Project project);

    /**
     * Unassign the given project from the given user.
     *
     * @param user    the user to be unassigned
     * @param project the target project
     */
    void unassignUserFromProject(User user, Project project);

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
     * @throws InvalidUserException if the user is incorrect
     */
    void storePassword(@NotNull String userIdentifier, @NotNull String plainPassword) throws InvalidUserException;

    /**
     * Updates the users password
     *
     * @param userIdentifier   the user which should get a new password
     * @param oldPlainPassword the users old password
     * @param newPlainPassword the users new password
     * @throws InvalidUserException     if the user is incorrect
     * @throws InvalidPasswordException if the password is incorrect
     */
    void changePassword(@NotNull String userIdentifier, @NotNull String oldPlainPassword,
                        @NotNull String newPlainPassword) throws InvalidUserException, InvalidPasswordException;

    /**
     * Checks if the given password checks against the given user.
     *
     * @param userIdentifier the user to check against
     * @param plainPassword  the users password to check
     * @throws InvalidPasswordException if the old password is incorrect
     * @throws InvalidUserException     if the user is incorrect
     */
    void checkIfPasswordIsValid(@NotNull String userIdentifier, @NotNull String plainPassword)
            throws InvalidPasswordException, InvalidUserException;

}
