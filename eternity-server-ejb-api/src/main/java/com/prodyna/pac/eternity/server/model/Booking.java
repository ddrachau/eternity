package com.prodyna.pac.eternity.server.model;

import java.util.Calendar;

/**
 * A booking is a concrete time booking with a start and end date (at the same day) for a project.
 */
public class Booking extends AbstractNode {

    /**
     * The start time.
     */
    private Calendar startTime;
    /**
     * The end time.
     */
    private Calendar endTime;
    /**
     * The break duration in minutes.
     */
    private int breakDuration;
    /**
     * An optional descript of the work
     */
    private String description;

    /**
     * Empty default constructor
     */
    public Booking() {

    }

    /**
     * Creates a booking and initialize the following properties:
     *
     * @param id            the technical identifier
     * @param startTime     the start time
     * @param endTime       the end time
     * @param breakDuration the break duration in minutes
     * @param description   the description of the work done
     */
    public Booking(String id, Calendar startTime, Calendar endTime, int breakDuration, String description) {
        super(id);
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakDuration = breakDuration;
        this.description = description;
    }

    /**
     * Creates a booking and initialize the following properties:
     *
     * @param startTime     the start time
     * @param endTime       the end time
     * @param breakDuration the break duration in minutes
     * @param description   the description of the work done
     */
    public Booking(Calendar startTime, Calendar endTime, int breakDuration, String description) {
        this(null, startTime, endTime, breakDuration, description);
    }

    /**
     * Creates a booking and initialize the following properties:
     *
     * @param startTime     the start time
     * @param endTime       the end time
     * @param breakDuration the break duration in minutes
     */
    public Booking(Calendar startTime, Calendar endTime, int breakDuration) {
        this(null, startTime, endTime, breakDuration, null);
    }

    /**
     * Basic Getter
     *
     * @return the startTime
     */
    public Calendar getStartTime() {
        return startTime;
    }

    /**
     * Basic Setter
     *
     * @param startTime to be set
     */
    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    /**
     * Basic Getter
     *
     * @return the endTime
     */
    public Calendar getEndTime() {
        return endTime;
    }

    /**
     * Basic Setter
     *
     * @param endTime to be set
     */
    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    /**
     * Basic Getter
     *
     * @return the breakDuration
     */
    public int getBreakDuration() {
        return breakDuration;
    }

    /**
     * Basic Setter
     *
     * @param breakDuration to be set
     */
    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    /**
     * Basic Getter
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Basic Setter
     *
     * @param description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Booking booking = (Booking) o;

        if (breakDuration != booking.breakDuration) return false;
        if (startTime != null ? !startTime.equals(booking.startTime) : booking.startTime != null) return false;
        if (endTime != null ? !endTime.equals(booking.endTime) : booking.endTime != null) return false;
        return !(description != null ? !description.equals(booking.description) : booking.description != null);

    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + this.getId() + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", breakDuration=" + breakDuration +
                ", description=" + description +
                '}';
    }

}
