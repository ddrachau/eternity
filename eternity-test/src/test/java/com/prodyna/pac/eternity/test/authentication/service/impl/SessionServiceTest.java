package com.prodyna.pac.eternity.test.authentication.service.impl;

import com.prodyna.pac.eternity.authentication.service.AuthenticationService;
import com.prodyna.pac.eternity.authentication.service.SessionService;
import com.prodyna.pac.eternity.project.service.ProjectService;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.authentication.Session;
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
import java.util.List;


@RunWith(Arquillian.class)
public class SessionServiceTest extends AbstractArquillianTest {

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

    @Test
    @InSequence(1)
    public void createDemoData() {

        databaseCleaner.deleteAllData();

    }

    @Test
    @InSequence(2)
    public void testGetAndCreate() throws ElementAlreadyExistsException {

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
    public void testDeleteByUser() throws ElementAlreadyExistsException {

        User u = userService.create(new User("deleteByUserSession", "foor", "suur", "pw123"));
        Session s = sessionService.create(u.getIdentifier());
        Assert.assertNotNull(s);
        Assert.assertNotNull(sessionService.get(s.getId()));

        sessionService.deleteByUser(u.getIdentifier());

        Assert.assertNull(sessionService.get(s.getId()));

        sessionService.create(u.getIdentifier());
        sessionService.create(u.getIdentifier());
        Assert.assertEquals(2, sessionService.getByUser(u.getIdentifier()).size());

        sessionService.deleteByUser(u.getIdentifier());

        Assert.assertEquals(0, sessionService.getByUser(u.getIdentifier()).size());

    }

    @Test
    @InSequence(4)
    public void testdeleteAndGetByUser() throws ElementAlreadyExistsException {

        User u = userService.create(new User("deleteSession", "foor", "suur", "pw123"));

        Assert.assertEquals(0, sessionService.getByUser(u.getIdentifier()).size());

        Session s = sessionService.create(u.getIdentifier());

        List<Session> sessions = sessionService.getByUser(u.getIdentifier());
        Assert.assertEquals(1, sessions.size());
        Assert.assertEquals(s, sessions.get(0));

        sessionService.create(u.getIdentifier());
        Assert.assertEquals(2, sessionService.getByUser(u.getIdentifier()).size());

        sessionService.delete(s.getId());

        Assert.assertEquals(1, sessionService.getByUser(u.getIdentifier()).size());

    }

}
