package com.prodyna.pac.eternity.client.rest.service.user.impl;

import com.prodyna.pac.eternity.client.rest.security.Authenticated;
import com.prodyna.pac.eternity.client.rest.service.user.UserClientService;
import com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils;
import com.prodyna.pac.eternity.server.model.booking.Booking;
import com.prodyna.pac.eternity.server.model.project.Project;
import com.prodyna.pac.eternity.server.model.user.User;
import com.prodyna.pac.eternity.server.service.booking.BookingService;
import com.prodyna.pac.eternity.server.service.project.ProjectService;
import com.prodyna.pac.eternity.server.service.user.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Default implementation for the UserClientService.
 */
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
    @Produces(RestCookieUtils.JSON_UTF8)
    @Override
    public Response get() {

        return Response.ok(userService.findAll()).build();

    }

    @PermitAll
    @GET
    @Produces(RestCookieUtils.JSON_UTF8)
    @Path("/session")
    @Override
    public Response getBySession(@HeaderParam(RestCookieUtils.HEADER_TOKEN_XSRF) final String xsrfToken) {

        return Response.ok(userService.getBySessionId(xsrfToken)).build();

    }

    @PermitAll
    @GET
    @Produces(RestCookieUtils.JSON_UTF8)
    @Path("/bookings")
    @Override
    public Response getBookings(@HeaderParam(RestCookieUtils.HEADER_TOKEN_XSRF) final String xsrfToken) {

        User user = userService.getBySessionId(xsrfToken);

        List<Booking> bookings = bookingService.findByUser(user);

        return Response.ok().entity(bookings).build();

    }

    @PermitAll
    @GET
    @Produces(RestCookieUtils.JSON_UTF8)
    @Path("/projects")
    @Override
    public Response getProjects(@HeaderParam(RestCookieUtils.HEADER_TOKEN_XSRF) final String xsrfToken) {

        User user = userService.getBySessionId(xsrfToken);

        List<Project> projects = projectService.findAllAssignedToUser(user);

        return Response.ok().entity(projects).build();

    }

}
