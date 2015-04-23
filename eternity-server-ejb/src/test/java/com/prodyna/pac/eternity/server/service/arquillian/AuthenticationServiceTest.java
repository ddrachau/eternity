package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.model.Login;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.*;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.prodyna.pac.eternity.server.common.PasswordHash.validatePassword;


@RunWith(Arquillian.class)
public class AuthenticationServiceTest extends AbstractArquillianTest {

    @Inject
    private CypherService cypherService;

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
    public void createDemoData() {

        // clean DB from nodes and relations
        cypherService.query(CLEANUP_QUERY, null);

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", null, "pw2");
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

        Assert.assertNotNull(user1.getPassword());
        Assert.assertTrue(validatePassword("pw", user1.getPassword()));
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
    public void testLoginWithRememberMe() throws InvalidLoginException {

        User u = userService.create(new User("loginWithRem", "for", "sur", "123"));

        Assert.assertNull(rememberMeService.getByUser(u.getIdentifier()));

        Login l = authenticationService.login(new Login(u.getIdentifier(), "123", false));

        Assert.assertNotNull(l);
        Assert.assertNotNull(l.getXsrfToken());
        Assert.assertNull(l.getRememberMeToken());

        Login l2 = authenticationService.login(new Login(u.getIdentifier(), "123", true));

        Assert.assertNotNull(l2);
        Assert.assertNotNull(l2.getXsrfToken());
        Assert.assertNotNull(l2.getRememberMeToken());
        Assert.assertFalse(l.getXsrfToken().equals(l2.getXsrfToken()));
        Assert.assertNotNull(rememberMeService.getByUser(u.getIdentifier()));

        Login l3 = authenticationService.login(new Login(u.getIdentifier(), "123", true));

        Assert.assertNotNull(l3);
        Assert.assertNotNull(l3.getXsrfToken());
        Assert.assertNotNull(l3.getRememberMeToken());
        Assert.assertFalse(l2.getRememberMeToken().equals(l3.getRememberMeToken()));
        Assert.assertNotNull(rememberMeService.getByUser(u.getIdentifier()));

        Login l4 = authenticationService.login(new Login(u.getIdentifier(), "123", false));

        Assert.assertNotNull(l4);
        Assert.assertNotNull(l4.getXsrfToken());
        Assert.assertNull(l4.getRememberMeToken());
        Assert.assertNull(rememberMeService.getByUser(u.getIdentifier()));

    }

    @Test(expected = InvalidPasswordException.class)
    @InSequence(4)
    public void testLoginWithWrongPassword() throws InvalidLoginException {

        User user1 = userService.get("khansen");

        Assert.assertNotNull(user1.getPassword());
        Assert.assertTrue(validatePassword("pw", user1.getPassword()));
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
        authenticationService.logout(l.getXsrfToken());
        Assert.assertNull(sessionService.get(l.getXsrfToken()));

    }

    @Test
    @InSequence(7)
    public void testLogoutWithRememberMe() throws InvalidLoginException {

        String username = "khansen";
        Assert.assertNull(rememberMeService.getByUser(username));
        Login l = authenticationService.login(new Login(username, "pw"));
        Assert.assertNull(rememberMeService.getByUser(username));
        Assert.assertNotNull(l);
        Assert.assertNotNull(sessionService.get(l.getXsrfToken()));
        authenticationService.logout(l.getXsrfToken());
        Assert.assertNull(rememberMeService.getByUser(username));
        Assert.assertNull(sessionService.get(l.getXsrfToken()));

        l = authenticationService.login(new Login(username, "pw", true));
        Assert.assertNotNull(rememberMeService.getByUser(username));
        Assert.assertNotNull(sessionService.get(l.getXsrfToken()));
        authenticationService.logout(l.getXsrfToken());
        Assert.assertNull(rememberMeService.getByUser(username));
        Assert.assertNull(sessionService.get(l.getXsrfToken()));

    }

}
