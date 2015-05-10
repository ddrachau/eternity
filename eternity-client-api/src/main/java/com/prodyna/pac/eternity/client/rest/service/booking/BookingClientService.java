package com.prodyna.pac.eternity.client.rest.service.booking;

import com.prodyna.pac.eternity.server.model.booking.Booking;

import javax.ws.rs.core.Response;

/**
 * Client service with operations for working with Bookings.
 */
public interface BookingClientService {

    Response createBooking(String xsrfToken, Booking booking);

    Response updateBooking(String xsrfToken, Booking booking);

    Response deleteBooking(String xsrfToken, String id);

}
