package com.prodyna.pac.eternity.server.rest.service;

import com.prodyna.pac.eternity.server.rest.filter.Authenticated;
import com.prodyna.pac.eternity.server.rest.utils.RestUtils;
import com.prodyna.pac.eternity.server.service.ProjectService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Authenticated
@Path("/project")
public class ProjectClientService {

    @Inject
    private ProjectService projectService;

    @GET
    @Produces(RestUtils.JSON_UTF8)
    public Response get() {
        return Response.ok(projectService.findAll()).build();
    }

}
