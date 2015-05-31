package com.prodyna.pac.eternity.common.model.booking;

import com.prodyna.pac.eternity.common.model.AbstractNode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Objects;

/**
 * A booking is a concrete time booking with a start and end date (at the same day) for a projectIdentifier.
 */
public class Booking extends AbstractNode {

    /**
     * The start time.
     */
    @NotNull
    private Calendar startTime;

    /**
     * The end time.
     */
    @NotNull
    private Calendar endTime;

    /**
     * The break duration in minutes.
     */
    @Min(0)
    private int breakDuration;

    /**
     * An optional description of the work
     */
    private String description;

    /**
     * The projectIdentifier identifier for this booking
     */
    private String userIdentifier;

    /**
     * The projectIdentifier identifier for this booking
     */
    private String projectIdentifier;

    /**
     * Empty default constructor
     */
    public Booking() {

    }

    /**
     * Creates a booking and initialize the following properties:
     *
     * @param id                the technical identifier
     * @param startTime         the start time
     * @param endTime           the end time
     * @param breakDuration     the break duration in minutes
     * @param description       the description of the work done
     * @param userIdentifier    the projectIdentifier identifier
     * @param projectIdentifier the projectIdentifier identifier
     */
    public Booking(final String id, final Calendar startTime, final Calendar endTime,
                   final int breakDuration, final String description, final String projectIdentifier,
                   final String userIdentifier) {

        super(id);
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakDuration = breakDuration;
        this.description = description;
        this.userIdentifier = userIdentifier;
        this.projectIdentifier = projectIdentifier;

    }

    /**
     * Creates a booking and initialize the following properties:
     *
     * @param startTime     the start time
     * @param endTime       the end time
     * @param breakDuration the break duration in minutes
     * @param description   the description of the work done
     */
    public Booking(final Calendar startTime, final Calendar endTime, final int breakDuration,
                   final String description) {

        this(null, startTime, endTime, breakDuration, description, null, null);

    }

    /**
     * Creates a booking and initialize the following properties:
     *
     * @param startTime     the start time
     * @param endTime       the end time
     * @param breakDuration the break duration in minutes
     */
    public Booking(final Calendar startTime, final Calendar endTime, final int breakDuration) {

        this(null, startTime, endTime, breakDuration, null, null, null);

    }

    /**
     * Basic Getter
     *
     * @return the startTime
     */
    public Calendar getStartTime() {

        return this.startTime;

    }

    /**
     * Basic Setter
     *
     * @param startTime to be set
     */
    public void setStartTime(final Calendar startTime) {

        this.startTime = startTime;

    }

    /**
     * Basic Getter
     *
     * @return the endTime
     */
    public Calendar getEndTime() {

        return this.endTime;

    }

    /**
     * Basic Setter
     *
     * @param endTime to be set
     */
    public void setEndTime(final Calendar endTime) {

        this.endTime = endTime;

    }

    /**
     * Basic Getter
     *
     * @return the breakDuration
     */
    public int getBreakDuration() {

        return this.breakDuration;

    }

    /**
     * Basic Setter
     *
     * @param breakDuration to be set
     */
    public void setBreakDuration(final int breakDuration) {

        this.breakDuration = breakDuration;

    }

    /**
     * Basic Getter
     *
     * @return the description
     */
    public String getDescription() {

        return this.description;

    }

    /**
     * Basic Setter
     *
     * @param description to be set
     */
    public void setDescription(final String description) {

        this.description = description;

    }

    /**
     * Basic Getter
     *
     * @return the userIdentifier
     */
    public String getUserIdentifier() {

        return this.userIdentifier;
    }

    /**
     * Basic Setter
     *
     * @param userIdentifier to be set
     */
    public void setUserIdentifier(final String userIdentifier) {

        this.userIdentifier = userIdentifier;
    }

    /**
     * Basic Getter
     *
     * @return the description
     */
    public String getProjectIdentifier() {

        return projectIdentifier;

    }

    /**
     * Basic Setter
     *
     * @param projectIdentifier to be set
     */
    public void setProjectIdentifier(final String projectIdentifier) {

        this.projectIdentifier = projectIdentifier;

    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(breakDuration, booking.breakDuration) &&
                Objects.equals(startTime, booking.startTime) &&
                Objects.equals(endTime, booking.endTime) &&
                Objects.equals(description, booking.description) &&
                Objects.equals(userIdentifier, booking.userIdentifier) &&
                Objects.equals(projectIdentifier, booking.projectIdentifier);
    }

    @Override
    public int hashCode() {

        return Objects.hash(startTime, endTime, breakDuration, description, userIdentifier, projectIdentifier);
    }

    @Override
    public String toString() {

        return "Booking{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", breakDuration=" + breakDuration +
                ", description='" + description + '\'' +
                ", userIdentifier='" + userIdentifier + '\'' +
                ", projectIdentifier='" + projectIdentifier + '\'' +
                '}';
    }

}
