package com.prodyna.pac.eternity.server.rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.prodyna.pac.eternity.server.rest.utils.RestCookieUtils.JSON_UTF8;

/**
 * Client service with operations for working with Users.
 */
public interface UserClientService {

    /**
     * Returns a list of all Users found.
     *
     * @return all user
     */
    @GET
    @Produces(JSON_UTF8)
    Response get();

}
