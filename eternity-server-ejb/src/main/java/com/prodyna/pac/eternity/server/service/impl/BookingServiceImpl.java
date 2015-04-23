package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.logging.Logging;
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
import com.prodyna.pac.eternity.server.service.ProjectService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.prodyna.pac.eternity.server.common.QueryUtils.map;
import static com.prodyna.pac.eternity.server.common.DateUtils.*;

@Logging
@Stateless
public class BookingServiceImpl implements BookingService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static String BOOKING_RETURN_PROPERTIES = "b.id, b.startTime, b.endTime, b.breakDuration, b.description";

    @Inject
    private CypherService cypherService;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

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

        this.checkForOverlapping(booking, user, project);

        booking.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {id:{1}}), (p:Project {id:{2}}) " +
                        "CREATE (u)<-[:PERFORMED_BY]-(b:Booking {id:{3}, startTime:{4}, endTime:{5}, breakDuration:{6}, description:{7}})-[:PERFORMED_FOR]->(p) " +
                        "RETURN " + BOOKING_RETURN_PROPERTIES,
                map(1, user.getId(), 2, project.getId(), 3, booking.getId(),
                        4, booking.getStartTime().getTimeInMillis(), 5, booking.getEndTime().getTimeInMillis(),
                        6, booking.getBreakDuration(), 7, booking.getDescription()));

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
    public Booking update(@NotNull Booking booking) throws DuplicateTimeBookingException, InvalidBookingException {

        this.checkIfBookingIsValid(booking);

        User user = userService.getByBooking(booking);
        Project project = projectService.get(booking);

        this.checkForOverlapping(booking, user, project);

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (b:Booking {id:{1}}) SET b.startTime={2}, b.endTime={3}, b.breakDuration={4}, b.description={5} " +
                        "RETURN " + BOOKING_RETURN_PROPERTIES,
                map(1, booking.getId(), 2, booking.getStartTime().getTimeInMillis(),
                        3, booking.getEndTime().getTimeInMillis(), 4, booking.getBreakDuration(),
                        5, booking.getDescription()));

        if (queryResult == null) {
            throw new NoSuchElementRuntimeException();
        } else {
            return this.getBooking(queryResult);
        }

    }

    @Override
    public void delete(@NotNull String id) throws NoSuchElementRuntimeException {

        if (this.get(id) == null) {
            throw new NoSuchElementRuntimeException();
        }

        cypherService.query(
                "MATCH ()<-[r1]-(b:Booking {id:{1}})-[r2]->() " +
                        "DELETE r1,b,r2",
                map(1, id));

    }

    /**
     * Validates if the booking is in the correct format.
     *
     * @param booking the booking to be checked
     * @throws InvalidBookingException if the given booking is inconsistent.
     */
    private void checkIfBookingIsValid(@NotNull Booking booking) throws InvalidBookingException {

        Calendar startTime = booking.getStartTime();
        Calendar endTime = booking.getEndTime();

        if (startTime == null || endTime == null) {
            throw new InvalidBookingException("Times not set");
        }

        if ((endTime.getTimeInMillis() - startTime.getTimeInMillis()) < 300000) {
            throw new InvalidBookingException("Start has to be at least 5min before end");
        }

        if ((endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 60000 < booking.getBreakDuration()) {
            throw new InvalidBookingException("Work has to be greater thand break");
        }

        boolean timesAtTheSameDay = startTime.get(Calendar.YEAR) == endTime.get(Calendar.YEAR) &&
                startTime.get(Calendar.DAY_OF_YEAR) == endTime.get(Calendar.DAY_OF_YEAR);
        if (!timesAtTheSameDay) {
            throw new InvalidBookingException("Bookings have to be at the same day");
        }

    }

    /**
     * Checks if the booking range overlaps with bookings found for the user project combination
     *
     * @param booking the times to check against
     * @param user    the source user
     * @param project the target user
     * @throws DuplicateTimeBookingException if there is an overlapping
     */
    private void checkForOverlapping(@NotNull Booking booking, @NotNull User user, @NotNull Project project)
            throws DuplicateTimeBookingException {

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User {id:{1}})<-[:PERFORMED_BY]-(b:Booking)-[:PERFORMED_FOR]->(p:Project {id:{2}}) " +
                        "WHERE " +
                        "(b.startTime >= {3} AND b.startTime < {4}) OR" +
                        "(b.endTime > {3} AND b.endTime <= {4}) OR" +
                        "(b.startTime <= {3} AND b.endTime >= {4}) " +
                        "RETURN " + BOOKING_RETURN_PROPERTIES,
                map(1, user.getId(), 2, project.getId(), 3, booking.getStartTime().getTimeInMillis(),
                        4, booking.getEndTime().getTimeInMillis()));
        if (queryResult.size() > 0) {
            throw new DuplicateTimeBookingException();
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
        String readDescription = (String) values.get("b.description");

        result.setId(readId);
        result.setStartTime(getCalendar(readStartTime));
        result.setEndTime(getCalendar(readEndTime));
        result.setBreakDuration(readBreakDuration);
        result.setDescription(readDescription);

        return result;

    }

}
