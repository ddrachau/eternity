package com.prodyna.pac.eternity.user.client.impl;

import com.prodyna.pac.eternity.booking.service.BookingService;
import com.prodyna.pac.eternity.common.client.Authenticated;
import com.prodyna.pac.eternity.common.helper.RestCookieBuilder;
import com.prodyna.pac.eternity.common.logging.Logging;
import com.prodyna.pac.eternity.common.profiling.Profiling;
import com.prodyna.pac.eternity.project.service.ProjectService;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.common.model.FilterRequest;
import com.prodyna.pac.eternity.common.model.FilterResponse;
import com.prodyna.pac.eternity.common.model.authentication.ChangePassword;
import com.prodyna.pac.eternity.common.model.authentication.SetPassword;
import com.prodyna.pac.eternity.common.model.booking.Booking;
import com.prodyna.pac.eternity.common.model.project.AssignProjects;
import com.prodyna.pac.eternity.common.model.project.Project;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.user.client.UserClientService;
import com.prodyna.pac.eternity.user.service.UserService;

import javax.annotation.security.PermitAll;
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
import java.util.List;

/**
 * Default implementation for the UserClientService.
 */
@Logging
@Profiling
@Authenticated
@Path("/users")
public class UserClientServiceImpl implements UserClientService {

    @Inject
    private UserService userService;

    @Inject
    private BookingService bookingService;

    @Inject
    private ProjectService projectService;

    @RolesAllowed({"MANAGER", "ADMINISTRATOR"})
    @GET
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Override
    public Response get(@HeaderParam(RestCookieBuilder.HEADER_TOKEN_XSRF) final String xsrfToken,
                        @QueryParam("sort") final String sort,
                        @QueryParam("filter") final String[] filter,
                        @QueryParam("start") final int start,
                        @QueryParam("pageSize") final int pageSize) {


        FilterRequest filterRequest = new FilterRequest(sort, filter, start, pageSize);
        FilterResponse<User> user = userService.find(filterRequest);

        return Response.ok().entity(user).build();

    }

    @PermitAll
    @GET
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("/session")
    @Override
    public Response getBySession(@HeaderParam(RestCookieBuilder.HEADER_TOKEN_XSRF) final String xsrfToken) {

        return Response.ok(userService.getBySessionId(xsrfToken)).build();

    }

    @PermitAll
    @GET
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("/bookings")
    @Override
    public Response getBookings(@HeaderParam(RestCookieBuilder.HEADER_TOKEN_XSRF) final String xsrfToken,
                                @QueryParam("sort") final String sort,
                                @QueryParam("filter") final String[] filter,
                                @QueryParam("start") final int start,
                                @QueryParam("pageSize") final int pageSize) {

        User user = userService.getBySessionId(xsrfToken);

        FilterRequest filterRequest = new FilterRequest(sort, filter, start, pageSize);
        FilterResponse<Booking> bookings = bookingService.findByUser(user, filterRequest);

        return Response.ok().entity(bookings).build();

    }

    @PermitAll
    @GET
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("/projects")
    @Override
    public Response getProjects(@HeaderParam(RestCookieBuilder.HEADER_TOKEN_XSRF) final String xsrfToken) {

        User user = userService.getBySessionId(xsrfToken);

        List<Project> projects = projectService.findAllAssignedToUser(user);

        return Response.ok().entity(projects).build();

    }

    @RolesAllowed({"MANAGER", "ADMINISTRATOR"})
    @GET
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("/{identifier}/projects")
    @Override
    public Response getAssignProjects(@PathParam("identifier") final String identifier) {

        User user = userService.get(identifier);

        AssignProjects result = new AssignProjects();

        result.setAssignedProjects(projectService.findAllAssignedToUser(user));
        result.setAssignableProjects(projectService.findAllAssignableToUser(user));

        return Response.ok().entity(result).build();

    }

    @RolesAllowed({"MANAGER", "ADMINISTRATOR"})
    @PUT
    @Path("/{identifier}/projects/{projectIdentifier}/assign")
    @Override
    public Response assignToProject(@PathParam("identifier") final String identifier,
                                    @PathParam("projectIdentifier") final String projectIdentifier) {

        User user = userService.get(identifier);
        Project project = projectService.get(projectIdentifier);

        userService.assignUserToProject(user, project);

        return Response.ok().build();

    }

    @RolesAllowed({"MANAGER", "ADMINISTRATOR"})
    @PUT
    @Path("/{identifier}/projects/{projectIdentifier}/unassign")
    @Override
    public Response unassignFromProject(@PathParam("identifier") final String identifier,
                                        @PathParam("projectIdentifier") final String projectIdentifier) {

        User user = userService.get(identifier);
        Project project = projectService.get(projectIdentifier);

        userService.unassignUserFromProject(user, project);

        return Response.ok().build();

    }

    @RolesAllowed({"ADMINISTRATOR"})
    @POST
    @Consumes(RestCookieBuilder.JSON_UTF8)
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Override
    public Response create(final User user) {

        try {

            userService.create(user);
            return Response.ok().build();

        } catch (ElementAlreadyExistsException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Id schon vorhanden\"}").build();
        }

    }

    @RolesAllowed({"ADMINISTRATOR"})
    @PUT
    @Consumes(RestCookieBuilder.JSON_UTF8)
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Override
    public Response update(final User user) {

        try {

            userService.update(user);
            return Response.ok().build();

        } catch (ElementAlreadyExistsException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Id schon vorhanden\"}").build();
        }

    }

    @RolesAllowed({"ADMINISTRATOR"})
    @POST
    @Consumes(RestCookieBuilder.JSON_UTF8)
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("{identifier}/password")
    @Override
    public Response setPassword(@PathParam("identifier") final String identifier,
                                final SetPassword setPassword) {

        try {
            userService.storePassword(identifier, setPassword.getNewPassword());
        } catch (InvalidUserException e) {
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity("{\"error\":\"" + "Benutzer Id nicht vorhanden\"}").build();
        }

        return Response.ok().build();

    }

    @PermitAll
    @PUT
    @Consumes(RestCookieBuilder.JSON_UTF8)
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("password")
    @Override
    public Response changePassword(@HeaderParam(RestCookieBuilder.HEADER_TOKEN_XSRF) final String xsrfToken,
                                   final ChangePassword changePassword) {

        try {

            User user = userService.getBySessionId(xsrfToken);
            userService.changePassword(user.getIdentifier(), changePassword.getOldPassword(),
                    changePassword.getNewPassword());
            return Response.ok().build();

        } catch (InvalidUserException e) {
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity("{\"error\":\"" + "Benutzer Id nicht vorhanden\"}").build();
        } catch (InvalidPasswordException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Passwort nicht korrekt\"}").build();
        }

    }

    @RolesAllowed({"ADMINISTRATOR"})
    @DELETE
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("{identifier}")
    @Override
    public Response delete(@PathParam("identifier") final String identifier) {

        userService.delete(identifier);
        return Response.ok().build();

    }

}
