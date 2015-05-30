package com.prodyna.pac.eternity.booking.client;

import com.prodyna.pac.eternity.common.model.booking.Booking;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Response;

/**
 * Client service with operations for working with Bookings.
 */
public interface BookingClientService {

    /**
     * Creates a booking with the given booking
     *
     * @param xsrfToken the current user token
     * @param booking   the booking data
     * @return the response from the execution
     */
    Response create(@NotNull @Size(min = 1) String xsrfToken, @NotNull @Valid Booking booking);

    /**
     * Updates a booking with the given booking
     *
     * @param xsrfToken the current user token
     * @param booking   the booking data
     * @return the response from the execution
     */
    Response update(@NotNull @Size(min = 1) String xsrfToken, @NotNull @Valid Booking booking);

    /**
     * Deletes a booking with the given id
     *
     * @param xsrfToken the current user token
     * @param id        the booking id
     * @return the response from the execution
     */
    Response delete(@NotNull @Size(min = 1) String xsrfToken, @NotNull @Size(min = 1) String id);

    /**
     * Returns a list of all bookings
     *
     * @param sort     optional sort order
     * @param filter   filter for field values
     * @param start    value to start from if pagination is used
     * @param pageSize the maximum result for a page call, &lt;=0 for no limit
     * @return the users bookings
     */
    Response get(String sort, String[] filter, int start, int pageSize);

}
