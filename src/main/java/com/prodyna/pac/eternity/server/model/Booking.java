package com.prodyna.pac.eternity.server.model;

import java.util.Date;

public class Booking {

    private Long id;
    private Date startTime;
    private Date endTime;
    private int breakDuration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (breakDuration != booking.breakDuration) return false;
        if (!id.equals(booking.id)) return false;
        if (startTime != null ? !startTime.equals(booking.startTime) : booking.startTime != null) return false;
        return !(endTime != null ? !endTime.equals(booking.endTime) : booking.endTime != null);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", breakDuration=" + breakDuration +
                '}';
    }
}
