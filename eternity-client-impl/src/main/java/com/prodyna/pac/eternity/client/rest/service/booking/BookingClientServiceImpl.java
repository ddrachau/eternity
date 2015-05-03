package com.prodyna.pac.eternity.client.rest.service.booking;

import com.prodyna.pac.eternity.client.rest.security.Authenticated;
import com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils;
import com.prodyna.pac.eternity.server.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.server.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.server.model.booking.Booking;
import com.prodyna.pac.eternity.server.model.project.Project;
import com.prodyna.pac.eternity.server.model.user.User;
import com.prodyna.pac.eternity.server.service.booking.BookingService;
import com.prodyna.pac.eternity.server.service.project.ProjectService;
import com.prodyna.pac.eternity.server.service.user.UserService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
    @POST
    @Consumes(RestCookieUtils.JSON_UTF8)
    @Override
    public Response createBooking(@HeaderParam(RestCookieUtils.HEADER_TOKEN_XSRF) final String xsrfToken, final Booking booking) {

        User u = userService.getBySessionId(xsrfToken);
        Project p = projectService.get(booking.getProjectIdentifier());

        try {
            bookingService.create(booking, u, p);
        } catch (DuplicateTimeBookingException e) {
            e.printStackTrace();
        } catch (UserNotAssignedToProjectException e) {
            e.printStackTrace();
        } catch (InvalidBookingException e) {
            e.printStackTrace();
        }

        return Response.ok().build();

    }

}
