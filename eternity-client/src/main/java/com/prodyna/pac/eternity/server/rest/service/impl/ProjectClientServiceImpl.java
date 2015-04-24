package com.prodyna.pac.eternity.server.rest.service.impl;

import com.prodyna.pac.eternity.server.rest.filter.Authenticated;
import com.prodyna.pac.eternity.server.rest.service.ProjectClientService;
import com.prodyna.pac.eternity.server.service.ProjectService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Default implementation for the ProjectClientService.
 */
@Authenticated
@Path("/project")
public class ProjectClientServiceImpl implements ProjectClientService {

    @Inject
    private ProjectService projectService;

    @Override
    public Response get() {

        return Response.ok(projectService.findAll()).build();

    }

}
