package com.prodyna.pac.eternity.client.service;

import com.prodyna.pac.eternity.client.helper.AbstractRESTTest;
import com.prodyna.pac.eternity.client.rest.service.AuthenticationClientService;
import com.prodyna.pac.eternity.helper.DatabaseCleaner;
import com.prodyna.pac.eternity.server.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.model.authentication.Login;
import com.prodyna.pac.eternity.server.model.user.User;
import com.prodyna.pac.eternity.server.service.user.UserService;
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

    @Test
    @RunAsClient
    @InSequence(2)
    public void testLogin() {

        AuthenticationClientService authenticationClientService = createService(AuthenticationClientService.class);

        Response response = authenticationClientService.login(null, null, null, new Login("khansen", "pw"));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(1, response.getCookies().size());
        Assert.assertNotNull(response.getEntity());

    }

//    @Test
//    @RunAsClient
//    @InSequence(2)
//    public void checkNoFound() {
//        ConferenceService conferenceService = createService(ConferenceService.class);
//        Assert.assertEquals(0, conferenceService.readAll().size());
//    }
//
//    @Test
//    @RunAsClient
//    @InSequence(3)
//    public void createConference() throws ParseException {
//        ConferenceService conferenceService = createService(ConferenceService.class);
//        DateFormatter dateFormatter = new DateFormatter();
//        Conference conf = new Conference("jfs", "Java Forum Stuttgart", dateFormatter.parse("2014-06-06"), dateFormatter.parse("2014-06-08"));
//        conferenceService.create(conf);
//    }
//
//    @Test
//    @RunAsClient
//    @InSequence(4)
//    public void createAnotherConference() throws ParseException {
//        ConferenceService conferenceService = createService(ConferenceService.class);
//        DateFormatter dateFormatter = new DateFormatter();
//        Conference conf = new Conference("jax", "JAX", dateFormatter.parse("2014-08-06"), dateFormatter.parse("2014-08-08"));
//        conferenceService.create(conf);
//    }
//
//    @Test
//    @RunAsClient
//    @InSequence(5)
//    public void readAll() {
//        ConferenceService conferenceService = createService(ConferenceService.class);
//        List<Conference> confs = conferenceService.readAll();
//        Assert.assertEquals(2, confs.size());
//    }
//
//    @Test
//    @RunAsClient
//    @InSequence(6)
//    public void readJfs() {
//        ConferenceService conferenceService = createService(ConferenceService.class);
//        Conference conf = conferenceService.readById("jfs");
//        Assert.assertEquals( "jfs", conf.getId() );
//    }
//
//    @Test
//    @RunAsClient
//    @InSequence(7)
//    public void readJax() {
//        ConferenceService conferenceService = createService(ConferenceService.class);
//        Conference conf = conferenceService.readById("jax");
//        Assert.assertEquals( "jax", conf.getId() );
//    }

}
