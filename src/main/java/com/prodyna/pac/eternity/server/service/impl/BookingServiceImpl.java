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
import com.prodyna.pac.eternity.server.service.UserService;

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

    @Inject
    private UserService userService;

    @Override
    public Booking create(@NotNull Booking booking, @NotNull User user, @NotNull Project project)
            throws DuplicateTimeBookingException, UserNotAssignedToProjectException, InvalidBookingException {

        this.checkIfBookingIsValid(booking);

        if (booking.getId() != null) {
            throw new InvalidBookingException("Booking already have an id");
        }

        if (!userService.isAssignedTo(user, project)) {
            throw new UserNotAssignedToProjectException(user.toString());
        }

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

//        // TODO get the user and project for the given booking , get other bookings and check
//        this.checkIfBookingIsValid(booking, user, project);
//
//        final Map<String, Object> queryResult = cypherService.querySingle(
//                "MATCH (b:Booking {id:{1}}) SET p.identifier={2}, p.description={3} RETURN p.id, p.identifier, p.description",
//                map(1, booking.getId(), 2, project.getIdentifier(), 3, project.getDescription()));
//
//        if (queryResult == null) {
//            throw new NoSuchElementRuntimeException();
//        } else {
//            return this.getBooking(queryResult);
//        }
        return null;

    }

    @Override
    public void delete(@NotNull String id) throws NoSuchElementRuntimeException {

    }

    /**
     * Validates if the booking is in the correct format.
     *
     * @param booking the booking to be checked
     * @throws InvalidBookingException if the given booking is inconsistent.
     */
    private void checkIfBookingIsValid(@NotNull Booking booking) throws InvalidBookingException {

        Date startTime = booking.getStartTime();
        Date endTime = booking.getEndTime();

        if (startTime == null || endTime == null) {
            throw new InvalidBookingException("Times not set");
        }

        if (endTime.getTime() - startTime.getTime() > 300000) {
            throw new InvalidBookingException("Start has to be at least 5min before end");
        }

        if ((endTime.getTime() - startTime.getTime()) / 60000 > booking.getBreakDuration()) {
            throw new InvalidBookingException("Work has to be greater thand break");
        }

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(startTime);
        cal2.setTime(endTime);
        boolean timesAtTheSameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        if (!timesAtTheSameDay) {
            throw new InvalidBookingException("Bookings have to be at the same day");
        }

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
