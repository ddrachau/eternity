package com.prodyna.pac.eternity.server.model;

import java.util.Date;

/**
 * A booking is a concrete time booking with a start and end date (at the same day) for a project.
 */
public class Booking extends AbstractNode {

    private Date startTime;
    private Date endTime;
    private int breakDuration;
    private Project bookedFor;
    private User bookedBy;

    /**
     * Empty default constructor *
     */
    public Booking() {

    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    public Project getBookedFor() {
        return bookedFor;
    }

    public void setBookedFor(Project bookedFor) {
        this.bookedFor = bookedFor;
    }

    public User getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(User bookedBy) {
        this.bookedBy = bookedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Booking booking = (Booking) o;

        if (breakDuration != booking.breakDuration) return false;
        if (bookedBy != null ? !bookedBy.equals(booking.bookedBy) : booking.bookedBy != null) return false;
        if (bookedFor != null ? !bookedFor.equals(booking.bookedFor) : booking.bookedFor != null) return false;
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
                ", bookedFor=" + bookedFor +
                ", bookedBy=" + bookedBy +
                '}';
    }

}
