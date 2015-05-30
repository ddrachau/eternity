package com.prodyna.pac.eternity.user.client;

import com.prodyna.pac.eternity.common.model.authentication.ChangePassword;
import com.prodyna.pac.eternity.common.model.authentication.SetPassword;
import com.prodyna.pac.eternity.common.model.user.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Response;

/**
 * Client service with operations for working with Users.
 */
public interface UserClientService {

    /**
     * Returns a list of all Users found.
     *
     * @param xsrfToken the session token
     * @param sort      optional sort order
     * @param filter    filter for field values
     * @param start     value to start from if pagination is used
     * @param pageSize  the maximum result for a page call, &lt;=0 for no limit
     * @return the matching user
     */
    Response get(String xsrfToken, String sort, String[] filter, int start, int pageSize);

    /**
     * Return the user associated to this session
     *
     * @param xsrfToken the session token
     * @return the associated session
     */
    Response getBySession(String xsrfToken);

    /**
     * Returns a list of all bookings for the User
     *
     * @param xsrfToken the session token
     * @param sort      optional sort order
     * @param filter    filter for field values
     * @param start     value to start from if pagination is used
     * @param pageSize  the maximum result for a page call, &lt;=0 for no limit
     * @return the users bookings
     */
    Response getBookings(String xsrfToken, String sort, String[] filter, int start, int pageSize);

    /**
     * Returns a list of all projects the user is assigned to
     *
     * @param xsrfToken the session token
     * @return the users assigned projects
     */
    Response getProjects(String xsrfToken);

    /**
     * Returns a list of all projects the user is and can be assigned to
     *
     * @param identifier the user identifier
     * @return projects the user is or can be  assigned to
     */
    Response getAssignProjects(@NotNull @Size(min = 1) String identifier);

    /**
     * Assigns the given user to the given project
     *
     * @param identifier        the user identifier
     * @param projectIdentifier the project identifier
     * @return the response from the execution
     */
    Response assignToProject(@NotNull @Size(min = 1) String identifier,
                             @NotNull @Size(min = 1) String projectIdentifier);

    /**
     * Unassigns the given user from the given project
     *
     * @param identifier        the user identifier
     * @param projectIdentifier the project identifier
     * @return the response from the execution
     */
    Response unassignFromProject(@NotNull @Size(min = 1) String identifier,
                                 @NotNull @Size(min = 1) String projectIdentifier);

    /**
     * Creates the given user
     *
     * @param user the data
     * @return the response from the execution
     */
    Response create(@NotNull @Valid User user);

    /**
     * Updates the given user
     *
     * @param user the data
     * @return the response from the execution
     */
    Response update(@NotNull @Valid User user);

    /**
     * Deletes the given user
     *
     * @param identifier the users identifier
     * @return the response from the execution
     */
    Response delete(@NotNull @Size(min = 1) String identifier);

    /**
     * Sets the password for the given user
     *
     * @param identifier  the users identifier
     * @param setPassword the new password
     * @return the response from the execution
     */
    Response setPassword(String identifier, @NotNull @Valid SetPassword setPassword);

    /**
     * Changes the password for the current user
     *
     * @param xsrfToken      the users session token
     * @param changePassword the change password dto
     * @return the response from the execution
     */
    Response changePassword(String xsrfToken, @NotNull @Valid ChangePassword changePassword);

}
