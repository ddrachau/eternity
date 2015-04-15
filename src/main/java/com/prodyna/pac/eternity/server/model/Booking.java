package com.prodyna.pac.eternity.server.model;

import java.util.Date;

/**
 * A booking is a concrete time booking with a start and end date (at the same day) for a project.
 */
public class Booking extends AbstractNode {

    /**
     * The start time.
     */
    private Date startTime;
    /**
     * The end time.
     */
    private Date endTime;
    /**
     * The break duration in minutes.
     */
    private int breakDuration;

    /**
     * Empty default constructor *
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
     */
    public Booking(String id, Date startTime, Date endTime, int breakDuration) {
        super(id);
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakDuration = breakDuration;
    }

    /**
     * Creates a booking and initialize the following properties:
     *
     * @param startTime     the start time
     * @param endTime       the end time
     * @param breakDuration the break duration in minutes
     */
    public Booking(Date startTime, Date endTime, int breakDuration) {
        this(null, startTime, endTime, breakDuration);
    }

    /**
     * Basic Getter
     *
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Basic Setter
     *
     * @param startTime to be set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Basic Getter
     *
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Basic Setter
     *
     * @param endTime to be set
     */
    public void setEndTime(Date endTime) {
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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Booking booking = (Booking) o;

        if (breakDuration != booking.breakDuration) return false;
        if (endTime != null ? !endTime.equals(booking.endTime) : booking.endTime != null) return false;
        if (startTime != null ? !startTime.equals(booking.startTime) : booking.startTime != null) return false;

        return super.equals(o);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + this.getId() + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", breakDuration=" + breakDuration +
                '}';
    }

}
