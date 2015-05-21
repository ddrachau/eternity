package com.prodyna.pac.eternity.client.rest.service.booking.impl;

import com.prodyna.pac.eternity.client.rest.security.Authenticated;
import com.prodyna.pac.eternity.client.rest.service.booking.BookingClientService;
import com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils;
import com.prodyna.pac.eternity.server.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.server.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.server.model.FilterRequest;
import com.prodyna.pac.eternity.server.model.FilterResponse;
import com.prodyna.pac.eternity.server.model.booking.Booking;
import com.prodyna.pac.eternity.server.model.project.Project;
import com.prodyna.pac.eternity.server.model.user.User;
import com.prodyna.pac.eternity.server.service.booking.BookingService;
import com.prodyna.pac.eternity.server.service.project.ProjectService;
import com.prodyna.pac.eternity.server.service.user.UserService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Default implementation for the UserClientService.
 */
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
    @Produces(RestCookieUtils.JSON_UTF8)
    @Override
    public Response get(@QueryParam("sort") final String sort, @QueryParam("filter") final String[] filter,
                        @QueryParam("start") final int start, @QueryParam("pageSize") final int pageSize) {

        FilterRequest filterRequest = new FilterRequest(sort, filter, start, pageSize);
        FilterResponse<Booking> bookings = bookingService.findAll(filterRequest);

        return Response.ok().entity(bookings).build();

    }

    @PermitAll
    @POST
    @Consumes(RestCookieUtils.JSON_UTF8)
    @Produces(RestCookieUtils.JSON_UTF8)
    @Override
    public Response create(@HeaderParam(RestCookieUtils.HEADER_TOKEN_XSRF) final String xsrfToken,
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
    @Consumes(RestCookieUtils.JSON_UTF8)
    @Produces(RestCookieUtils.JSON_UTF8)
    @Override
    public Response update(@HeaderParam(RestCookieUtils.HEADER_TOKEN_XSRF) final String xsrfToken,
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
    @Produces(RestCookieUtils.JSON_UTF8)
    @Path("{id}")
    @Override
    public Response delete(@HeaderParam(RestCookieUtils.HEADER_TOKEN_XSRF) final String xsrfToken,
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
