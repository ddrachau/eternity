package com.prodyna.pac.eternity.test.common.model.authentication;

import com.prodyna.pac.eternity.common.model.authentication.Session;
import junit.framework.Assert;
import org.junit.Test;

import javax.validation.constraints.AssertFalse;
import java.util.Calendar;

/**
 * DTO Test
 */
public class SessionTest {

    @Test
    public void test() {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        Session session = new Session();

        Assert.assertNull(session.getCreatedTime());
        Assert.assertNull(session.getLastAccessedTime());

        session.setCreatedTime(cal1);
        session.setLastAccessedTime(cal2);

        Assert.assertEquals(cal1, session.getCreatedTime());
        Assert.assertEquals(cal2, session.getLastAccessedTime());

        Assert.assertNotNull(session.toString());
        Assert.assertTrue(session.hashCode() != 0);
        Assert.assertFalse(session.equals(null));

    }

}

