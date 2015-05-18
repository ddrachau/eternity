package com.prodyna.pac.eternity.client.rest.service.user;

import com.prodyna.pac.eternity.server.model.authentication.ChangePassword;
import com.prodyna.pac.eternity.server.model.user.User;

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

    Response create(User user);

    Response update(User user);

    Response delete(String identifier);

    Response setPassword(String identifier, ChangePassword changePassword);

    Response changePassword(String xsrfToken, String identifier, ChangePassword changePassword);

}
