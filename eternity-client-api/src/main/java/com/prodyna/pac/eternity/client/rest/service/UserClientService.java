package com.prodyna.pac.eternity.client.rest.service;

import javax.ws.rs.core.Cookie;
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
     * @param xsrfCookie the session cookie
     * @return the associated session
     */
    Response getBySession(Cookie xsrfCookie);

}
