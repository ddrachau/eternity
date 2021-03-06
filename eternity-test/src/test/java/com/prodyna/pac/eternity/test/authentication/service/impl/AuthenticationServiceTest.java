package com.prodyna.pac.eternity.test.authentication.service.impl;

import com.prodyna.pac.eternity.authentication.service.AuthenticationService;
import com.prodyna.pac.eternity.authentication.service.RememberMeService;
import com.prodyna.pac.eternity.authentication.service.SessionService;
import com.prodyna.pac.eternity.project.service.ProjectService;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidTokenException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.common.model.authentication.Login;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.test.helper.AbstractArquillianTest;
import com.prodyna.pac.eternity.test.helper.DatabaseCleaner;
import com.prodyna.pac.eternity.user.service.UserService;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;


@RunWith(Arquillian.class)
public class AuthenticationServiceTest extends AbstractArquillianTest {

    @Inject
    private DatabaseCleaner databaseCleaner;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private SessionService sessionService;

    @Inject
    private RememberMeService rememberMeService;

    @Test
    @InSequence(1)
    public void createDemoData() throws ElementAlreadyExistsException {

        databaseCleaner.deleteAllData();

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", "A", "pw2");
        User user3 = new User("rvoeller", "Rudi", "Völler", null);
        User user4 = new User("bborg", "Björn", "Borg", "pw");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);

    }

    @Test
    @InSequence(2)
    public void testLogin() throws InvalidLoginException {

        User user1 = userService.get("khansen");

        Login l = authenticationService.login(new Login(user1.getIdentifier(), "pw"));
        Assert.assertNotNull(l);
        String sId = l.getXsrfToken();
        Assert.assertNotNull(sId);
        Assert.assertEquals(sId, sessionService.get(l.getXsrfToken()).getId());

        l = authenticationService.login(new Login(user1.getIdentifier(), "pw"));
        Assert.assertNotNull(l);
        Assert.assertFalse(sId.equals(l.getXsrfToken()));

    }

    @Test
    @InSequence(3)
    public void testLoginWithRememberMe() throws InvalidLoginException, ElementAlreadyExistsException {

        User u = userService.create(new User("loginWithRem", "for", "sur", "123"));

        Assert.assertTrue(rememberMeService.getByUser(u.getIdentifier()).isEmpty());

        Login l = authenticationService.login(new Login(u.getIdentifier(), "123", false));

        Assert.assertNotNull(l);
        Assert.assertNotNull(l.getXsrfToken());
        Assert.assertNull(l.getRememberMeToken());

        Login l2 = authenticationService.login(new Login(u.getIdentifier(), "123", true));

        Assert.assertNotNull(l2);
        Assert.assertNotNull(l2.getXsrfToken());
        Assert.assertNotNull(l2.getRememberMeToken());
        Assert.assertFalse(l.getXsrfToken().equals(l2.getXsrfToken()));
        Assert.assertFalse(rememberMeService.getByUser(u.getIdentifier()).isEmpty());

        Login l3 = authenticationService.login(new Login(u.getIdentifier(), "123", true));

        Assert.assertNotNull(l3);
        Assert.assertNotNull(l3.getXsrfToken());
        Assert.assertNotNull(l3.getRememberMeToken());
        Assert.assertFalse(l2.getRememberMeToken().equals(l3.getRememberMeToken()));
        Assert.assertEquals(2, rememberMeService.getByUser(u.getIdentifier()).size());

        Login l4 = authenticationService.login(new Login(u.getIdentifier(), "123", false));

        Assert.assertNotNull(l4);
        Assert.assertNotNull(l4.getXsrfToken());
        Assert.assertNull(l4.getRememberMeToken());
        Assert.assertEquals(2, rememberMeService.getByUser(u.getIdentifier()).size());

    }

    @Test(expected = InvalidPasswordException.class)
    @InSequence(4)
    public void testLoginWithWrongPassword() throws InvalidLoginException {

        User user1 = userService.get("khansen");

        authenticationService.login(new Login(user1.getIdentifier(), "pw2"));

    }

    @Test(expected = InvalidUserException.class)
    @InSequence(5)
    public void testLoginWithUnknownUser() throws InvalidLoginException {

        authenticationService.login(new Login("unknown", "pw-irrelevant"));

    }

    @Test
    @InSequence(6)
    public void testLogout() throws InvalidLoginException {

        Login l = authenticationService.login(new Login("khansen", "pw"));
        Assert.assertNotNull(l);
        Assert.assertNotNull(sessionService.get(l.getXsrfToken()));
        authenticationService.logout(l.getXsrfToken(), l.getRememberMeToken());
        Assert.assertNull(sessionService.get(l.getXsrfToken()));

    }

    @Test
    @InSequence(7)
    public void testLogoutWithRememberMe() throws InvalidLoginException {

        String username = "bborg";
        Assert.assertTrue(rememberMeService.getByUser(username).isEmpty());
        Login l = authenticationService.login(new Login(username, "pw"));
        Assert.assertTrue(rememberMeService.getByUser(username).isEmpty());
        Assert.assertNotNull(l);
        Assert.assertNotNull(sessionService.get(l.getXsrfToken()));
        authenticationService.logout(l.getXsrfToken(), l.getRememberMeToken());
        Assert.assertTrue(rememberMeService.getByUser(username).isEmpty());
        Assert.assertNull(sessionService.get(l.getXsrfToken()));

        l = authenticationService.login(new Login(username, "pw", true));
        Assert.assertFalse(rememberMeService.getByUser(username).isEmpty());
        Assert.assertNotNull(sessionService.get(l.getXsrfToken()));
        authenticationService.logout(l.getXsrfToken(), l.getRememberMeToken());
        Assert.assertTrue(rememberMeService.getByUser(username).isEmpty());
        Assert.assertNull(sessionService.get(l.getXsrfToken()));

    }

    @Test
    @InSequence(8)
    public void testLoginWithToken() throws ElementAlreadyExistsException, InvalidLoginException {

        User u = userService.create(new User("tokenUser", "Tok", "en", "1234"));

        Login l = authenticationService.login(new Login(u.getIdentifier(), "1234", true));
        Login l2 = authenticationService.login(l.getRememberMeToken());

        Assert.assertNotNull(l);
        Assert.assertNotNull(l2);
        Assert.assertEquals(l.getUsername(), l2.getUsername());
        Assert.assertFalse(l.getRememberMeToken().equals(l2.getRememberMeToken()));
        Assert.assertFalse(l.getXsrfToken().equals(l2.getXsrfToken()));

        try {
            authenticationService.login("");
            Assert.fail();
        } catch (InvalidTokenException e) {
            // expected
        }

        try {
            authenticationService.login("ahjsdhakjsdkjashda");
            Assert.fail();
        } catch (InvalidTokenException e) {
            // expected
        }

        try {
            authenticationService.login("sdflijfld:sjdfsdjfl");
            Assert.fail();
        } catch (InvalidTokenException e) {
            // expected
        }
    }

    @Test
    @InSequence(9)
    public void testLoginWithUserWithoutAPassword() throws ElementAlreadyExistsException, InvalidLoginException {

        User u = userService.create(new User("uwoapass", "Tok", "en", ""));

        try {
            authenticationService.login(new Login(u.getIdentifier(), ""));
            Assert.fail("login with empty password should not be possible");
        } catch (RuntimeException e) {
            // expected
        }

        User u2 = userService.create(new User("uwoapass2", "Tok", "en", null));

        try {
            authenticationService.login(new Login(u2.getIdentifier(), ""));
            Assert.fail("login with empty password should not be possible");
        } catch (RuntimeException e) {
            // expected
        }
        try {
            authenticationService.login(new Login(u2.getIdentifier(), null));
            Assert.fail("login with empty password should not be possible");
        } catch (RuntimeException e) {
            // expected
        }

    }

}
