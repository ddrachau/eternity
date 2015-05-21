package com.prodyna.pac.eternity.client.rest.service.booking;

import com.prodyna.pac.eternity.server.model.booking.Booking;

import javax.ws.rs.core.Response;

/**
 * Client service with operations for working with Bookings.
 */
public interface BookingClientService {

    Response create(String xsrfToken, Booking booking);

    Response update(String xsrfToken, Booking booking);

    Response delete(String xsrfToken, String id);

    /**
     * Returns a list of all bookings
     *
     * @param sort      optional sort order
     * @param filter    filter for field values
     * @param start     value to start from if pagination is used
     * @param pageSize  the maximum result for a page call, &lt;=0 for no limit
     * @return the users bookings
     */
    Response get(String sort, String[] filter, int start, int pageSize);

}
