package com.prodyna.pac.eternity.server.common;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Helper class for consistent Date behavior in the application
 */
public abstract class DateUtils {

    /**
     * Get a UTC Date for the given parameter.
     *
     * @param year   the year
     * @param month  the month
     * @param day    the day
     * @param hour   the hour
     * @param minute the minute
     * @return the created Date
     */
    public static Date getUTCDate(int year, int month, int day, int hour, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, 0);

        return calendar.getTime();
    }

    /**
     * Get a UTC Date for the given milliseconds.
     *
     * @param milliseconds the date in milliseconds
     * @return the created Date
     */
    public static Date getUTCDate(long milliseconds) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        return calendar.getTime();
    }

    /**
     * Returns a calendar for the given date.
     *
     * @param date the source date
     * @return a calendar for the date
     */
    public static Calendar getCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        return calendar;
    }

}
