package com.prodyna.pac.eternity.booking.client.impl;

import com.prodyna.pac.eternity.booking.client.BookingClientService;
import com.prodyna.pac.eternity.booking.service.BookingService;
import com.prodyna.pac.eternity.common.client.Authenticated;
import com.prodyna.pac.eternity.common.helper.RestCookieBuilder;
import com.prodyna.pac.eternity.common.logging.Logging;
import com.prodyna.pac.eternity.common.model.FilterRequest;
import com.prodyna.pac.eternity.common.model.FilterResponse;
import com.prodyna.pac.eternity.common.model.booking.Booking;
import com.prodyna.pac.eternity.common.model.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.common.model.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.common.model.project.Project;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.common.profiling.Profiling;
import com.prodyna.pac.eternity.project.service.ProjectService;
import com.prodyna.pac.eternity.user.service.UserService;

import javax.annotation.security.PermitAll;
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

/**
 * Default implementation for the UserClientService.
 */
@Logging
@Profiling
@Authenticated
@Path("/bookings")
public class BookingClientServiceImpl implements BookingClientService {

    @Inject
    private UserService userService;

    @Inject
    private BookingService bookingService;

    @Inject
    private ProjectService projectService;

    @PermitAll
    @GET
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Override
    public Response get(@QueryParam("sort") final String sort, @QueryParam("filter") final String[] filter,
                        @QueryParam("start") final int start, @QueryParam("pageSize") final int pageSize) {

        FilterRequest filterRequest = new FilterRequest(sort, filter, start, pageSize);
        FilterResponse<Booking> bookings = bookingService.find(filterRequest);

        return Response.ok().entity(bookings).build();

    }

    @PermitAll
    @POST
    @Consumes(RestCookieBuilder.JSON_UTF8)
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Override
    public Response create(@HeaderParam(RestCookieBuilder.HEADER_TOKEN_XSRF) final String xsrfToken,
                           final Booking booking) {

        Response.ResponseBuilder responseBuilder = Response.ok();

        try {

            User u = userService.getBySessionId(xsrfToken);
            Project p = projectService.get(booking.getProjectIdentifier());
            bookingService.create(booking, u, p);

        } catch (DuplicateTimeBookingException e) {
            responseBuilder = Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Überschneidende Buchung" + "\"}");
        } catch (UserNotAssignedToProjectException e) {
            responseBuilder = Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity("{\"error\":\"" + "Benutzer ist dem Projekt nicht zugeordnet" + "\"}");
        } catch (InvalidBookingException e) {
            responseBuilder = Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Ungültige Buchung: (" + e.getMessage() + ")\"}");
        }

        return responseBuilder.build();

    }

    @PermitAll
    @PUT
    @Consumes(RestCookieBuilder.JSON_UTF8)
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Override
    public Response update(@HeaderParam(RestCookieBuilder.HEADER_TOKEN_XSRF) final String xsrfToken,
                           final Booking booking) {

        Response.ResponseBuilder responseBuilder = Response.ok();

        try {

            bookingService.update(booking);

        } catch (DuplicateTimeBookingException e) {
            responseBuilder = Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Überschneidende Buchung" + "\"}");
        } catch (InvalidBookingException e) {
            responseBuilder = Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity("{\"error\":\"" + "Ungültige Buchung: (" + e.getMessage() + ")\"}");
        }

        return responseBuilder.build();

    }

    @PermitAll
    @DELETE
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("{id}")
    @Override
    public Response delete(@HeaderParam(RestCookieBuilder.HEADER_TOKEN_XSRF) final String xsrfToken,
                           @PathParam("id") final String id) {

        User requestUser = userService.getBySessionId(xsrfToken);
        Booking b = bookingService.get(id);
        User bookingUser = userService.getByBooking(b);

        Response.ResponseBuilder responseBuilder = Response.ok();

        if (requestUser.getIdentifier().equals(bookingUser.getIdentifier())) {

            bookingService.delete(id);

        } else {

            responseBuilder = Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"" + "Benutzer darf keine Bookings anderer Benutzer löschen" + "\"}");

        }

        return responseBuilder.build();
    }

}
