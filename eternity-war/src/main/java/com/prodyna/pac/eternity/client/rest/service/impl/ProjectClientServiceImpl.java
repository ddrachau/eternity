package com.prodyna.pac.eternity.client.rest.service.impl;

import com.prodyna.pac.eternity.client.rest.service.ProjectClientService;
import com.prodyna.pac.eternity.client.rest.security.Authenticated;
import com.prodyna.pac.eternity.server.service.project.ProjectService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.JSON_UTF8;

/**
 * Default implementation for the ProjectClientService.
 */
@Authenticated
@Path("/project")
public class ProjectClientServiceImpl implements ProjectClientService {

    @Inject
    private ProjectService projectService;

    @RolesAllowed({"MANAGER","ADMINISTRATOR"})
    @GET
    @Produces(JSON_UTF8)
    @Override
    public Response get() {

        return Response.ok(projectService.findAll()).build();

    }

}
