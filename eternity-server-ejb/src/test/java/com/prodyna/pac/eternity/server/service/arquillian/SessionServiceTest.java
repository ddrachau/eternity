package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.*;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;


@RunWith(Arquillian.class)
public class SessionServiceTest extends AbstractArquillianTest {

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

    @Test
    @InSequence(1)
    public void createDemoData() {

        // clean DB from nodes and relations
        cypherService.query(CLEANUP_QUERY, null);

    }

    @Test
    @InSequence(2)
    public void testGetAndCreate() {

        User u = userService.create(new User("createSession", "foor", "suur", "pw123"));
        Session s = sessionService.create("createSession");
        Assert.assertNotNull(s);
        Session s2 = sessionService.get(s.getId());
        Assert.assertNotNull(s2);

        Assert.assertEquals(s.getId(), s2.getId());
        Assert.assertEquals(s.getCreatedTime(), s2.getCreatedTime());
        Assert.assertTrue(s.getLastAccessedTime().getTimeInMillis() < s2.getLastAccessedTime().getTimeInMillis());

    }

    @Test
    @InSequence(3)
    public void testDeleteByUser() {

        User u = userService.create(new User("deleteSession", "foor", "suur", "pw123"));
        Session s = sessionService.create(u.getIdentifier());
        Assert.assertNotNull(s);
        Assert.assertNotNull(sessionService.get(s.getId()));

        sessionService.deleteByUser(u.getIdentifier());

        Assert.assertNull(sessionService.get(s.getId()));

    }
}
