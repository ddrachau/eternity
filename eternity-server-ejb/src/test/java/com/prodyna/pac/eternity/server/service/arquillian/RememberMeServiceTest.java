package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.model.RememberMe;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.*;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;


@RunWith(Arquillian.class)
public class RememberMeServiceTest extends AbstractArquillianTest {

    @Inject
    private CypherService cypherService;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Inject
    private AuthenticationService authenticationService;

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
    public void testCreateAndGet() {


        User u = userService.create(new User("createRememberMe", "foor", "suur", "pw123"));
        RememberMe r = rememberMeService.create(u.getIdentifier());
        Assert.assertNotNull(r);
        RememberMe r2 = rememberMeService.get(r.getId());
        Assert.assertNotNull(r2);

        Assert.assertEquals(r.getId(), r2.getId());
        Assert.assertEquals(r.getHashedToken(), r2.getHashedToken());
        Assert.assertNotNull(r.getToken());
        Assert.assertNull(r2.getToken());

    }

    @Test
    @InSequence(3)
    public void testDeleteByUser() {

        User u = userService.create(new User("deleteRememberMe", "foor", "suur", "pw123"));
        RememberMe r = rememberMeService.create(u.getIdentifier());
        Assert.assertNotNull(r);
        Assert.assertNotNull(rememberMeService.get(r.getId()));

        rememberMeService.deleteByUser(u.getIdentifier());

        Assert.assertNull(rememberMeService.get(r.getId()));

    }

    @Test
    @InSequence(4)
    public void testGetByUser() {

        User u = userService.create(new User("getByUser", "foor", "suur", "pw123"));
        RememberMe r = rememberMeService.create(u.getIdentifier());
        Assert.assertNotNull(r);
        Assert.assertNotNull(rememberMeService.get(r.getId()));

        rememberMeService.deleteByUser(u.getIdentifier());

        Assert.assertNull(rememberMeService.get(r.getId()));

    }

}
