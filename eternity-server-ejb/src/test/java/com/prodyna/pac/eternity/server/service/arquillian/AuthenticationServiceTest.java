package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
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

import javax.ejb.EJBException;
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
    public void testLogin() throws NoSuchElementRuntimeException, InvalidPasswordException {

        User user1 = userService.get("khansen");

        Assert.assertNotNull(user1.getPassword());
        Assert.assertTrue(validatePassword("pw", user1.getPassword()));
        Session s = authenticationService.login(user1, "pw");
        Assert.assertNotNull(s);
        String sId = s.getId();
        Assert.assertNotNull(sId);
        Assert.assertEquals(s, authenticationService.getSession(s.getId()));

        s = authenticationService.login(user1, "pw");
        Assert.assertNotNull(s);
        Assert.assertFalse(sId.equals(s.getId()));

    }

    @Test(expected = InvalidPasswordException.class)
    @InSequence(3)
    public void testLoginWithWrongPassword() throws NoSuchElementRuntimeException, InvalidPasswordException {

        User user1 = userService.get("khansen");

        Assert.assertNotNull(user1.getPassword());
        Assert.assertTrue(validatePassword("pw", user1.getPassword()));
        authenticationService.login(user1, "pw2");

        Assert.fail("Cannot login with wrong password");

    }

    @Test
    @InSequence(4)
    public void testLoginWithUnknownUser() throws InvalidPasswordException {

        User notValidUser = new User("unknown", "fore", "sur", "pw");

        try {
            authenticationService.login(notValidUser, "pw-irrelevant");
            Assert.fail("Cannot login with an unknown user");
        } catch (EJBException ex) {
            if (ex.getCause() instanceof NoSuchElementRuntimeException) {
                return;
            }
            Assert.fail("Unexpected exception: " + ex);
        }

    }

    @Test
    @InSequence(5)
    public void testLogout() throws InvalidPasswordException {

        User user1 = userService.get("khansen");

        Session s = authenticationService.login(user1, "pw");
        Assert.assertNotNull(s);
        Assert.assertEquals(s, authenticationService.getSession(s.getId()));
        authenticationService.logout(s.getId());
        Assert.assertNull(authenticationService.getSession(s.getId()));

    }

    @Test
    @InSequence(6)
    public void testStorePassword() throws NoSuchElementRuntimeException {

        User user2 = userService.get("aeich");
        User user3 = userService.get("rvoeller");

        Assert.assertNotNull(user2.getPassword());
        Assert.assertNull(user3.getPassword());

        Assert.assertTrue(validatePassword("pw2", user2.getPassword()));
        Assert.assertFalse(validatePassword("pw", user2.getPassword()));

        String newPassword = "new";

        Assert.assertFalse(validatePassword(newPassword, user2.getPassword()));
        user2 = authenticationService.storePassword(user2, newPassword);
        Assert.assertTrue(validatePassword(newPassword, user2.getPassword()));

        user3 = authenticationService.storePassword(user3, newPassword);
        Assert.assertTrue(validatePassword(newPassword, user3.getPassword()));

    }

    @Test
    @InSequence(7)
    public void testStorePasswordWithUnkownUser() {

        User notValidUser = new User("unknown", "fore", "sur", "pw");

        try {
            authenticationService.storePassword(notValidUser, "newPw");
            Assert.fail("Cannot set password for an unknown user");
        } catch (EJBException ex) {
            if (ex.getCause() instanceof NoSuchElementRuntimeException) {
                return;
            }
            Assert.fail("Unexpected exception: " + ex);
        }

    }

    @Test
    @InSequence(8)
    public void testChangePassword() throws InvalidPasswordException, NoSuchElementRuntimeException {

        String newPassword = "boom";
        String oldPassword = "pw";
        User user4 = userService.get("bborg");

        Assert.assertTrue(validatePassword(oldPassword, user4.getPassword()));
        user4 = authenticationService.changePassword(user4, oldPassword, newPassword);
        Assert.assertFalse(validatePassword(oldPassword, user4.getPassword()));
        Assert.assertTrue(validatePassword(newPassword, user4.getPassword()));

    }

    @Test(expected = InvalidPasswordException.class)
    @InSequence(9)
    public void testChangeWithInvalidPassword() throws InvalidPasswordException, NoSuchElementRuntimeException {

        User user1 = userService.get("khansen");
        authenticationService.changePassword(user1, "wronpw", "newPass");

        Assert.fail("Cannot change password with an invalid password");

    }

    @Test
    @InSequence(10)
    public void testChangeWithUnknownUser() throws InvalidPasswordException {

        User notValidUser = new User("unknown", "fore", "sur", "pw");

        try {
            authenticationService.changePassword(notValidUser, "old", "newPw");
            Assert.fail("Cannot change password for an unknown user");
        } catch (EJBException ex) {
            if (ex.getCause() instanceof NoSuchElementRuntimeException) {
                return;
            }
            Assert.fail("Unexpected exception: " + ex);
        }

    }

}
