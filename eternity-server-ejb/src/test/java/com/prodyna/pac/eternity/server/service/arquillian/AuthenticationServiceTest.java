package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.ProjectService;
import com.prodyna.pac.eternity.server.service.UserService;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Calendar;

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
        Session s = authenticationService.login(user1.getIdentifier(), "pw");
        Assert.assertNotNull(s);
        String sId = s.getId();
        Assert.assertNotNull(sId);
        Assert.assertEquals(sId, authenticationService.getSession(s.getId()).getId());

        s = authenticationService.login(user1.getIdentifier(), "pw");
        Assert.assertNotNull(s);
        Assert.assertFalse(sId.equals(s.getId()));

    }

    @Test(expected = InvalidPasswordException.class)
    @InSequence(3)
    public void testLoginWithWrongPassword() throws InvalidLoginException {

        User user1 = userService.get("khansen");

        Assert.assertNotNull(user1.getPassword());
        Assert.assertTrue(validatePassword("pw", user1.getPassword()));
        authenticationService.login(user1.getIdentifier(), "pw2");

        Assert.fail("Cannot login with wrong password");

    }

    @Test(expected = InvalidUserException.class)
    @InSequence(4)
    public void testLoginWithUnknownUser() throws InvalidLoginException {

        authenticationService.login("unknown", "pw-irrelevant");
        Assert.fail("Cannot login with an unknown user");

    }

    @Test
    @InSequence(5)
    public void testLogout() throws InvalidLoginException {

        Session s = authenticationService.login("khansen", "pw");
        Assert.assertNotNull(s);
        Assert.assertNotNull(authenticationService.getSession(s.getId()));
        authenticationService.logout(s.getId());
        Assert.assertNull(authenticationService.getSession(s.getId()));

    }

    @Test
    @InSequence(6)
    public void testStorePassword() throws InvalidUserException {

        User user2 = userService.get("aeich");
        User user3 = userService.get("rvoeller");

        Assert.assertNotNull(user2.getPassword());
        Assert.assertNull(user3.getPassword());

        Assert.assertTrue(validatePassword("pw2", user2.getPassword()));
        Assert.assertFalse(validatePassword("pw", user2.getPassword()));

        String newPassword = "new";

        Assert.assertFalse(validatePassword(newPassword, user2.getPassword()));
        authenticationService.storePassword("aeich", newPassword);
        user2 = userService.get("aeich");
        Assert.assertTrue(validatePassword(newPassword, user2.getPassword()));

        authenticationService.storePassword("rvoeller", newPassword);
        user3 = userService.get("rvoeller");
        Assert.assertTrue(validatePassword(newPassword, user3.getPassword()));

    }

    @Test(expected = InvalidUserException.class)
    @InSequence(7)
    public void testStorePasswordWithUnkownUser() throws InvalidUserException {

        User notValidUser = new User("unknown", "fore", "sur", "pw");

        authenticationService.storePassword("unknown", "newPw");

    }

    @Test
    @InSequence(8)
    public void testChangePassword() throws InvalidLoginException {

        String newPassword = "boom";
        String oldPassword = "pw";
        User user4 = userService.get("bborg");

        Assert.assertTrue(validatePassword(oldPassword, user4.getPassword()));
        authenticationService.changePassword("bborg", oldPassword, newPassword);
        user4 = userService.get("bborg");
        Assert.assertFalse(validatePassword(oldPassword, user4.getPassword()));
        Assert.assertTrue(validatePassword(newPassword, user4.getPassword()));

    }

    @Test(expected = InvalidPasswordException.class)
    @InSequence(9)
    public void testChangeWithInvalidPassword() throws InvalidLoginException {

        authenticationService.changePassword("khansen", "wronpw", "newPass");

        Assert.fail("Cannot change password with an invalid password");

    }

    @Test(expected = InvalidUserException.class)
    @InSequence(10)
    public void testChangeWithUnknownUser() throws InvalidLoginException {

        authenticationService.changePassword("unknown", "old", "newPw");
        Assert.fail("Cannot change password for an unknown user");

    }

    @Test
    @InSequence(11)
    public void testGetSession() throws InvalidLoginException {

        User user4 = userService.get("bborg");
        Session s1 = authenticationService.login("bborg", "boom");

        Assert.assertNotNull(s1);
        Assert.assertNotNull(s1.getId());
        Calendar c1 = s1.getCreatedTime();
        Calendar c2 = s1.getLastAccessedTime();
        Assert.assertNotNull(c1);
        Assert.assertNotNull(c2);

        Session s2 = authenticationService.getSession(s1.getId());
        Assert.assertNotNull(s2);
        Assert.assertNotNull(s2.getId());
        Assert.assertEquals(s1.getId(), s2.getId());
        Calendar c3 = s2.getCreatedTime();
        Calendar c4 = s2.getLastAccessedTime();
        Assert.assertEquals(c1, c3);
        Assert.assertTrue(c2.getTimeInMillis() < c4.getTimeInMillis());

    }
}
