package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsRuntimeException;
import com.prodyna.pac.eternity.server.exception.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.ProjectService;
import com.prodyna.pac.eternity.server.service.UserService;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
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
    public void createDemoData() throws Exception {

        // clean DB from nodes and relations
        cypherService.query("MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r", null);

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
    public void testLogin() throws NoSuchElementRuntimeException, InvalidPasswordException {

        User user1 = userService.get("khansen");

        Assert.assertNotNull(user1.getPassword());
        Assert.assertTrue(validatePassword("pw", user1.getPassword()));
        authenticationService.login(user1, "pw");

    }

    @Test(expected = InvalidPasswordException.class)
    public void testLoginWithWrongPassword() throws NoSuchElementRuntimeException, InvalidPasswordException {

        User user1 = userService.get("khansen");

        Assert.assertNotNull(user1.getPassword());
        Assert.assertTrue(validatePassword("pw", user1.getPassword()));
        authenticationService.login(user1, "pw2");

        Assert.fail("Cannot login with wrong password");

    }

    @Test
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
    public void testLogout() {

        Assert.fail();

    }

    @Test
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
    public void testChangeWithInvalidPassword() throws InvalidPasswordException, NoSuchElementRuntimeException {

        User user1 = userService.get("khansen");
        authenticationService.changePassword(user1, "wronpw", "newPass");

        Assert.fail("Cannot change password with an invalid password");

    }

    @Test
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
