package com.prodyna.pac.eternity.server.rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.prodyna.pac.eternity.server.rest.utils.RestUtils.JSON_UTF8;

/**
 * Client service with operations for working with Projects.
 */
public interface ProjectClientService {

    /**
     * Returns a list of all Projects found.
     *
     * @return all projects
     */
    @GET
    @Produces(JSON_UTF8)
    Response get();

}
