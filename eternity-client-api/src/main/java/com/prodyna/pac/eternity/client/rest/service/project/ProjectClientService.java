package com.prodyna.pac.eternity.client.rest.service.project;

import com.prodyna.pac.eternity.server.model.project.Project;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    /**
     * Creates a project
     *
     * @param project the project data
     * @return the response from the execution
     */
    Response create(@NotNull @Valid Project project);

    /**
     * Updates the given project
     *
     * @param project the project data
     * @return the response from the execution
     */
    Response update(@NotNull @Valid Project project);

    /**
     * Deletes the project with the given identifier
     *
     * @param identifier the project to delete
     * @return the response from the execution
     */
    Response delete(@NotNull @Size(min = 1) String identifier);

}
