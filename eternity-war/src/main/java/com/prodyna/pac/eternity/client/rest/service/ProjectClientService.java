package com.prodyna.pac.eternity.client.rest.service;

import javax.ws.rs.core.Response;

/**
 * Client service with operations for working with Projects.
 */
public interface ProjectClientService {

    /**
     * Returns a list of all Projects found.
     *
     * @return all projects
     */
    Response get();

}
