package com.prodyna.pac.eternity.test.common.model.authentication;

import com.prodyna.pac.eternity.common.model.authentication.Login;
import com.prodyna.pac.eternity.common.model.user.User;
import junit.framework.Assert;
import org.junit.Test;

/**
 * DTO Test
 */
public class LoginTest {

    @Test
    public void test() {

        String username = "user";
        String password = "password";
        String newUsername = "nuser";
        String newPassword = "npassword";
        String xsrf = "abc";
        String rToken = "123";
        User u = new User("id", "for", "sur", "pw");

        Login login1 = new Login();
        Login login2 = new Login(username, password);
        Login login3 = new Login(username, password, false);

        Assert.assertNotNull(login1.toString());

        Assert.assertEquals(username, login2.getUsername());
        Assert.assertEquals(password, login2.getPassword());
        Assert.assertFalse(login3.isRemember());

        login3.setPassword(newPassword);
        login3.setUsername(newUsername);

        Assert.assertEquals(newUsername, login3.getUsername());
        Assert.assertEquals(newPassword, login3.getPassword());

        Assert.assertNull(login1.getRememberMeToken());
        Assert.assertNull(login1.getXsrfToken());
        Assert.assertNull(login1.getUser());

        login1.setRememberMeToken(rToken);
        login1.setXsrfToken(xsrf);
        login1.setUser(u);

        Assert.assertEquals(rToken, login1.getRememberMeToken());
        Assert.assertEquals(xsrf, login1.getXsrfToken());
        Assert.assertEquals(u, login1.getUser());

    }

}
