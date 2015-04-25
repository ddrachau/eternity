package com.prodyna.pac.eternity.server.rest.service;

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

}
