package com.prodyna.pac.eternity.server.service.booking;

import com.prodyna.pac.eternity.server.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.server.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.server.model.booking.Booking;
import com.prodyna.pac.eternity.server.model.project.Project;
import com.prodyna.pac.eternity.server.model.user.User;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Basic service for operating on bookings.
 */
@Local
public interface BookingService {

    /**
     * Creates a time booking for the given user and project.
     *
     * @param booking the booking to be created
     * @param user    the user who spent the time
     * @param project the project the user spent his time on
     * @return the created Booking
     * @throws DuplicateTimeBookingException     if there already exists a booking which would overlap with this new one for the user and project
     * @throws UserNotAssignedToProjectException if the user is not allowed to book on this project
     * @throws InvalidBookingException           if the booking is inconsistent
     */
    Booking create(@NotNull Booking booking, @NotNull User user, @NotNull Project project)
            throws DuplicateTimeBookingException, UserNotAssignedToProjectException, InvalidBookingException;

    /**
     * Searches for a single booking.
     *
     * @param id the if to search for
     * @return the found booking or null if none was found
     */
    Booking get(String id);

    /**
     * Search for all the bookings of an user.
     *
     * @param user the source user
     * @return bookings which are assigned to the user, empty list if the user does not exists or no booking was made
     */
    List<Booking> findByUser(@NotNull User user);

    /**
     * Search for all the bookings for a project.
     *
     * @param project the source project
     * @return bookings which are assigned to the project, empty list if the project does not exists or no booking was made
     */
    List<Booking> findByProject(@NotNull Project project);

    /**
     * Search for the bookings made by a user for a project.
     *
     * @param user    the source user
     * @param project the source project
     * @return bookings for the user and project, empty list if the user or project do not exist or no booking was made
     */
    List<Booking> findByUserAndProject(@NotNull User user, @NotNull Project project);

    /**
     * Updates the given booking in the database.
     *
     * @param booking the booking to be updated
     * @return the updated booking
     * @throws DuplicateTimeBookingException if there already exists a booking which would overlap with this new one for the user and project
     * @throws InvalidBookingException       if the booking is inconsistent
     */
    Booking update(@NotNull Booking booking) throws DuplicateTimeBookingException, InvalidBookingException;

    /**
     * Removes the given booking from the database.
     *
     * @param id the id of the booking to be deleted
     */
    void delete(@NotNull String id);

}
