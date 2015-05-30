package com.prodyna.pac.eternity.common.helper.impl;

import com.prodyna.pac.eternity.common.helper.CalendarBuilder;

import java.util.Calendar;

/**
 * Default implementation for a CalendarBuilder
 */
public class CalendarBuilderImpl implements CalendarBuilder {

    @Override
    public Calendar getCalendar(final int year, final int month, final int day, final int hour, final int minute) {

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

    @Override
    public Calendar getCalendar(final long milliseconds) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        return calendar;

    }

    @Override
    public Calendar getNow() {

        Calendar calendar = Calendar.getInstance();

        return calendar;

    }

}
