package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.common.logging.Logging;
import com.prodyna.pac.eternity.server.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.server.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.server.model.Booking;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.BookingService;
import com.prodyna.pac.eternity.server.service.CypherService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.prodyna.pac.eternity.server.common.QueryUtils.map;

@Logging
@Stateless
public class BookingServiceImpl implements BookingService {

    private static String BOOKING_RETURN_PROPERTIES = "b.id, b.startTime, b.endTime, b.breakDuration";

    @Inject
    private CypherService cypherService;

    @Override
    public Booking create(@NotNull Booking booking, @NotNull User user, @NotNull Project project)
            throws DuplicateTimeBookingException, UserNotAssignedToProjectException, InvalidBookingException {

        // TODO check if the user is allowed to book
        // TODO check if end is after start, same day, break is not greater than work, work, at least 5min
        // TODO check if a booking for overlaps exists

        booking.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {id:{1}}), (p:Project {id:{2}}) " +
                        "CREATE (u)<-[:PERFORMED_BY]-(b:Booking {id:{3}, startTime:{4}, endTime: {5}, breakDuration: {6}})-[:PERFORMED_FOR]->(p) " +
                        "RETURN " + BOOKING_RETURN_PROPERTIES,
                map(1, user.getId(), 2, project.getId(), 3, booking.getId(),
                        4, booking.getStartTime().getTime(), 5, booking.getEndTime().getTime(), 6, booking.getBreakDuration()));

        if (queryResult == null) {
            throw new NotCreatedRuntimeException(booking.toString());
        }

        return booking;

    }

    @Override
    public Booking get(String id) {

        Booking result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (b:Booking {id:{1}}) RETURN " + BOOKING_RETURN_PROPERTIES,
                map(1, id));

        if (queryResult != null) {
            result = this.getBooking(queryResult);
        }

        return result;

    }

    @Override
    public List<Booking> findByUser(@NotNull User user) {

        List<Booking> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User {id:{1}})<-[:PERFORMED_BY]-(b:Booking) " +
                        "RETURN " + BOOKING_RETURN_PROPERTIES,
                map(1, user.getId()));

        for (Map<String, Object> values : queryResult) {
            result.add(this.getBooking(values));
        }

        return result;

    }

    @Override
    public List<Booking> findByProject(@NotNull Project project) {

        List<Booking> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (p:Project {id:{1}})<-[:PERFORMED_FOR]-(b:Booking) " +
                        "RETURN " + BOOKING_RETURN_PROPERTIES,
                map(1, project.getId()));

        for (Map<String, Object> values : queryResult) {
            result.add(this.getBooking(values));
        }

        return result;

    }

    @Override
    public List<Booking> findByUserAndProject(@NotNull User user, @NotNull Project project) {

        List<Booking> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User {id:{1}})<-[:PERFORMED_BY]-(b:Booking)-[:PERFORMED_FOR]->(p:Project {id:{2}}) " +
                        "RETURN " + BOOKING_RETURN_PROPERTIES,
                map(1, user.getId(), 2, project.getId()));

        for (Map<String, Object> values : queryResult) {
            result.add(this.getBooking(values));
        }

        return result;

    }

    @Override
    public Booking update(@NotNull Booking booking) throws NoSuchElementRuntimeException, DuplicateTimeBookingException {
        return null;
    }

    @Override
    public void delete(@NotNull String id) throws NoSuchElementRuntimeException {

    }

    /**
     * Helper method to construct a Booking from a query response.
     *
     * @param values the available values
     * @return a filled Booking
     */
    private Booking getBooking(Map<String, Object> values) {

        Booking result = new Booking();

        String readId = (String) values.get("b.id");
        long readStartTime = (long) values.get("b.startTime");
        long readEndTime = (long) values.get("b.endTime");
        int readBreakDuration = (int) values.get("b.breakDuration");


        result.setId(readId);
        result.setStartTime(new Date(readStartTime));
        result.setEndTime(new Date(readEndTime));
        result.setBreakDuration(readBreakDuration);

        return result;

    }

}
