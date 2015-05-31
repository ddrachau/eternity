package com.prodyna.pac.eternity.test.common.model.authentication;

import com.prodyna.pac.eternity.common.model.authentication.ChangePassword;
import junit.framework.Assert;
import org.junit.Test;

/**
 * DTO Test
 */
public class ChangePasswordTest {

    @Test
    public void test() {

        ChangePassword changePassword = new ChangePassword();

        Assert.assertNull(changePassword.getNewPassword());
        Assert.assertNull(changePassword.getOldPassword());

        String newPassword = "pw";
        String oldPassword = "old";

        changePassword.setNewPassword(newPassword);
        changePassword.setOldPassword(oldPassword);

        Assert.assertEquals(newPassword, changePassword.getNewPassword());
        Assert.assertEquals(oldPassword, changePassword.getOldPassword());

        Assert.assertNotNull(changePassword.toString());

    }

}
