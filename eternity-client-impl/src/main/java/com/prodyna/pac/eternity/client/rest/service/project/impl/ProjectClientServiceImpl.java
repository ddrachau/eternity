package com.prodyna.pac.eternity.client.rest.service.project.impl;

import com.prodyna.pac.eternity.client.rest.security.Authenticated;
import com.prodyna.pac.eternity.client.rest.service.project.ProjectClientService;
import com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils;
import com.prodyna.pac.eternity.server.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.model.FilterRequest;
import com.prodyna.pac.eternity.server.model.FilterResponse;
import com.prodyna.pac.eternity.server.model.project.Project;
import com.prodyna.pac.eternity.server.service.project.ProjectService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.JSON_UTF8;

/**
 * Default implementation for the ProjectClientService.
 */
@Authenticated
@Path("/projects")
public class ProjectClientServiceImpl implements ProjectClientService {

    @Inject
    private ProjectService projectService;

    @RolesAllowed({"MANAGER", "ADMINISTRATOR"})
    @GET
    @Produces(JSON_UTF8)
    @Override
    public Response get(@HeaderParam(RestCookieUtils.HEADER_TOKEN_XSRF) final String xsrfToken,
                        @QueryParam("sort") final String sort,
                        @QueryParam("filter") final String[] filter,
                        @QueryParam("start") final int start,
                        @QueryParam("pageSize") final int pageSize) {

        FilterRequest filterRequest = new FilterRequest(sort, filter, start, pageSize);
        FilterResponse<Project> projects = projectService.find(filterRequest);

        return Response.ok().entity(projects).build();

    }

    @RolesAllowed({"ADMINISTRATOR"})
    @POST
    @Consumes(RestCookieUtils.JSON_UTF8)
    @Produces(RestCookieUtils.JSON_UTF8)
    @Override
    public Response create(final Project project) {

        try {

            projectService.create(project);
            return Response.ok().build();

        } catch (ElementAlreadyExistsException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Id schon vorhanden\"}").build();
        }

    }

    @RolesAllowed({"ADMINISTRATOR"})
    @PUT
    @Consumes(RestCookieUtils.JSON_UTF8)
    @Produces(RestCookieUtils.JSON_UTF8)
    @Override
    public Response update(final Project project) {

        try {

            projectService.update(project);
            return Response.ok().build();

        } catch (ElementAlreadyExistsException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Id schon vorhanden\"}").build();
        }

    }

    @RolesAllowed({"ADMINISTRATOR"})
    @DELETE
    @Produces(RestCookieUtils.JSON_UTF8)
    @Path("{identifier}")
    @Override
    public Response delete(@PathParam("identifier") final String identifier) {

        projectService.delete(identifier);
        return Response.ok().build();

    }

}
