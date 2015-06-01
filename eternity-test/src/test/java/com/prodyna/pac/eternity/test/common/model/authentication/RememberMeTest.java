package com.prodyna.pac.eternity.test.common.model.authentication;

import com.prodyna.pac.eternity.common.model.authentication.RememberMe;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * DTO Test
 */
public class RememberMeTest {

    @Test
    public void test() {

        Calendar cal = Calendar.getInstance();
        String hasedToken = "asdas";
        String token = "ab";

        RememberMe rememberMe = new RememberMe();

        Assert.assertNull(rememberMe.getCreatedTime());
        Assert.assertNull(rememberMe.getHashedToken());
        Assert.assertNull(rememberMe.getToken());

        rememberMe.setCreatedTime(cal);
        rememberMe.setHashedToken(hasedToken);
        rememberMe.setToken(token);

        Assert.assertEquals(cal, rememberMe.getCreatedTime());
        Assert.assertEquals(hasedToken, rememberMe.getHashedToken());
        Assert.assertEquals(token, rememberMe.getToken());

        Assert.assertNotNull(rememberMe.toString());

    }

}
