package com.prodyna.pac.eternity.server.arquillian;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.UserService;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

@RunWith(Arquillian.class)
public class UserServiceTest {

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "eternity-arq.jar").addPackages(true, "com.prodyna.pac");
        jar.addAsResource("META-INF/beans.xml");
        jar.addAsResource("META-INF/persistence.xml");
        System.out.println(jar.toString(true));
        return jar;
    }

    @Inject
    private UserService userService;

    @Inject
    private CypherService cypherService;

    @Test
    @InSequence(1)
    public void createDemoData() throws Exception {

        // clean DB from nodes and relations
        cypherService.query("MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r", null);

        userService.create(new User("khansen", "Knut", "Hansen", "pw"));
        userService.create(new User("aeich", "Alexander", null, "pw2"));
        userService.create(new User("rvoeller", "Rudi", "Völler", "pw3"));
        userService.create(new User("bborg", "Bjönr", "Borg", "pw"));

    }

    @Test
    @InSequence(2)
    public void testGetAllUsers() {
        Assert.assertEquals(4, userService.findAll().size());
    }

    @Test
    @InSequence(3)
    public void testCreateUser() throws Exception {

        String identifier = "ddemo";
        String forename = "Dirk";
        String surname = "Demo";

        User u = new User(identifier, forename, surname, "pw");

        Assert.assertNull(u.getId());

        User u2 = userService.create(u);

        Assert.assertSame(u, u2);
        Assert.assertNotNull(u2.getId());
        Assert.assertEquals(identifier, u2.getIdentifier());
        Assert.assertEquals(forename, u2.getForename());
        Assert.assertEquals(surname, u2.getSurname());

    }

    @Test(expected = ElementAlreadyExistsException.class)
    @InSequence(4)
    public void testCreateUserWhichExists() throws ElementAlreadyExistsException {

        String identifier = "new identifier";

        User u = new User(identifier, null, null, null);

        userService.create(u);
        userService.create(u);
        Assert.fail("Unique constraint has to prohibit the creation");

    }

    @Test
    @InSequence(5)
    public void testGetUser() {

        String identifier = "ddemo";
        String forename = "Dirk";
        String surname = "Demo";

        User u = userService.get(identifier);
        Assert.assertNotNull(u);
        Assert.assertEquals(identifier, u.getIdentifier());
        Assert.assertEquals(identifier, u.getIdentifier());
        Assert.assertEquals(forename, u.getForename());
        Assert.assertEquals(surname, u.getSurname());

    }

    @Test
    @InSequence(6)
    public void testGetUserUnknown() {

        Assert.assertNull(userService.get("unknownId"));

    }

    @Test
    @InSequence(7)
    public void testUpdateUser() throws ElementAlreadyExistsException, NoSuchElementException {

        String identifier = "aeich";
        String forename = "Alexander";
        String newForename = "Alexandar";

        User u = userService.get(identifier);
        Assert.assertNotNull(u);

        Assert.assertEquals(forename, u.getForename());
        Assert.assertNull(u.getSurname());
        u.setForename(newForename);

        u = userService.update(u);

        Assert.assertNotNull(u);
        Assert.assertEquals(newForename, u.getForename());

        u = userService.get(identifier);

        Assert.assertNotNull(u);
        Assert.assertEquals(newForename, u.getForename());

    }

    @Test(expected = ElementAlreadyExistsException.class)
    @InSequence(8)
    public void testUpdateUserExistingIdentifier() throws ElementAlreadyExistsException, NoSuchElementException {

        String identifier = "aeich";
        String newIdentifier = "khansen";

        User u = userService.get(identifier);
        Assert.assertNotNull(u);

        u.setIdentifier(newIdentifier);

        userService.update(u);

        Assert.fail("Changing to an already present identifier should not be possible");

    }

    @Test(expected = NoSuchElementException.class)
    @InSequence(9)
    public void testUpdateUserNonExistingNode() throws ElementAlreadyExistsException, NoSuchElementException {

        User u = new User("unknow", null, null, null);
        userService.update(u);

        Assert.fail("Changing an not present element should not be possible");

    }

    @Test
    @InSequence(10)
    public void testDeleteUser() throws NoSuchElementException {

        String identifier = "rvoeller";

        User u = userService.get(identifier);

        Assert.assertNotNull(u.getId());
        Assert.assertEquals(identifier, u.getIdentifier());

        userService.delete(identifier);

        Assert.assertNull(userService.get(identifier));

    }

    @Test(expected = NoSuchElementException.class)
    @InSequence(11)
    public void testDeleteUserNoSuchUser() throws NoSuchElementException {

        String identifier = "P01244";

        User u = userService.get(identifier);

        Assert.assertNull(u);

        userService.delete(identifier);

        Assert.fail("Node should not be present any more");

    }

    @Test
    @InSequence(12)
    public void testAssignUserToProject() throws NoSuchElementException {

        Assert.fail("not implemented");

    }

    @Test
    @InSequence(13)
    public void testUnassignUserFromProject() throws NoSuchElementException {

        Assert.fail("not implemented");

    }

    @Test
    @InSequence(14)
    public void testFindAllAssignedToProject() throws NoSuchElementException {

        Assert.fail("not implemented");

    }

}
