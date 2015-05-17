package com.prodyna.pac.eternity.client.rest.service.project;

import com.prodyna.pac.eternity.server.model.project.Project;

import javax.ws.rs.core.Response;

/**
 * Client service with operations for working with Projects.
 */
public interface ProjectClientService {

    /**
     * Returns a list of all Projects found.
     *
     * @param xsrfToken the session token
     * @param sort      optional sort order
     * @param filter    filter for field values
     * @param start     value to start from if pagination is used
     * @param pageSize  the maximum result for a page call, &lt;=0 for no limit
     * @return the matching projects
     */
    Response get(String xsrfToken, String sort, String[] filter, int start, int pageSize);

    Response create(Project project);

    Response update(Project project);

    Response delete(String identifier);

}
