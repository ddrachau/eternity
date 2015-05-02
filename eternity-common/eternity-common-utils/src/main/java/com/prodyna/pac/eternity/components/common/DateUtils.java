package com.prodyna.pac.eternity.components.common;

import java.util.Calendar;

/**
 * Helper class for consistent Date behavior in the application
 */
public abstract class DateUtils {

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
    public static Calendar getCalendar(int year, int month, int day, int hour, int minute) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;

    }

    /**
     * Get a calendar for the given milliseconds.
     *
     * @param milliseconds the date in milliseconds
     * @return the created calendar
     */
    public static Calendar getCalendar(long milliseconds) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        return calendar;

    }

    /**
     * Get a calendar for now.
     *
     * @return the created calendar
     */
    public static Calendar getNow() {

        Calendar calendar = Calendar.getInstance();

        return calendar;

    }

}
