package com.prodyna.pac.eternity.client.rest.service.user;

import javax.ws.rs.core.Response;

/**
 * Client service with operations for working with Users.
 */
public interface UserClientService {

    /**
     * Returns a list of all Users found.
     *
     * @return all user
     */
    Response get();

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
     * @return the users bookingsx
     */
    Response getBookings(String xsrfToken);

    /**
     * Returns a list of all projects the user is assigned to
     *
     * @param xsrfToken the session token
     * @return the users assigned projects
     */
    Response getProjects(String xsrfToken);

}
