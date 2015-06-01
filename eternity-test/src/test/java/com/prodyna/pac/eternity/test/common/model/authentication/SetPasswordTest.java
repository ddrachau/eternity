package com.prodyna.pac.eternity.test.common.model.authentication;

import com.prodyna.pac.eternity.common.model.authentication.SetPassword;
import junit.framework.Assert;
import org.junit.Test;

/**
 * DTO Test
 */
public class SetPasswordTest {

    @Test
    public void test() {

        String newPassword = "1234";

        SetPassword setPassword = new SetPassword();

        Assert.assertNull(setPassword.getNewPassword());

        setPassword.setNewPassword(newPassword);

        Assert.assertEquals(newPassword, setPassword.getNewPassword());
        Assert.assertNotNull(setPassword.toString());

    }

}
