package com.prodyna.pac.eternity.test.authentication.client.impl;

import com.prodyna.pac.eternity.common.model.authentication.Login;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.test.helper.AbstractRESTTest;
import com.prodyna.pac.eternity.test.helper.DatabaseCleaner;
import com.prodyna.pac.eternity.user.service.UserService;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * A simple test that uses the REST interface and tests the @see ConferenceService remotely.
 */
@RunWith(Arquillian.class)
public class AuthenticationClientServiceRESTTest extends AbstractRESTTest {

    @Inject
    private DatabaseCleaner databaseCleaner;

    @Inject
    private UserService userService;

    @Test
    @InSequence(1)
    public void testData() throws ElementAlreadyExistsException {

        databaseCleaner.deleteAllData();
        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        userService.create(user1);

        Assert.assertTrue(userService.find().size() > 0);

    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void testLogin() throws InterruptedException {

        AuthenticationClientServiceProxy authenticationClientService = createService(AuthenticationClientServiceProxy.class);

        String username = "khansen";
        Response response = authenticationClientService.login(new Login(username, "pw"));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(1, response.getCookies().size());

        User user = response.readEntity(User.class);
        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getIdentifier());
        Assert.assertNull(user.getPassword());

    }

}
