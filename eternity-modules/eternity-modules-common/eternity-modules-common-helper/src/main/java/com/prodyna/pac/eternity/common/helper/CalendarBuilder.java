package com.prodyna.pac.eternity.common.helper;

import java.util.Calendar;

/**
 * Helper class for consistent Date behavior in the application
 */
public interface CalendarBuilder {

    /**
     * Get a calendar for the given parameter.
     *
     * @param year   the year
     * @param month  the month
     * @param day    the day
     * @param hour   the hour
     * @param minute the minute
     * @return the created calendar
     */
    Calendar getCalendar(int year, int month, int day, int hour, int minute);

    /**
     * Get a calendar for the given milliseconds.
     *
     * @param milliseconds the date in milliseconds
     * @return the created calendar
     */
    Calendar getCalendar(long milliseconds);

    /**
     * Get a calendar for now.
     *
     * @return the created calendar
     */
    Calendar getNow();

}
