package com.prodyna.pac.eternity.test.common.model.booking;

import com.prodyna.pac.eternity.common.model.booking.Booking;
import junit.framework.Assert;
import org.junit.Test;

/**
 * DTO Test
 */
public class BookingTest {

    @Test
    public void test() {

        Booking booking = new Booking();

        Assert.assertNotNull(booking.toString());

    }

}
