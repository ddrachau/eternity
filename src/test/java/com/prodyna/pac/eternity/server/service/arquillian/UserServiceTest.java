package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsRuntimeException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
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
import java.util.List;

@RunWith(Arquillian.class)
public class UserServiceTest extends AbstractArquillianTest {

    @Inject
    private CypherService cypherService;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Test
    @InSequence(1)
    public void createDemoData() throws Exception {

        // clean DB from nodes and relations
        cypherService.query("MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r", null);

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", null, "pw2");
        User user3 = new User("rvoeller", "Rudi", "Völler", "pw3");
        User user4 = new User("bborg", "Björn", "Borg", "pw");
        User user5 = new User("hmeiser", "Hans", "Meiser", "pw");
        User user6 = new User("ttester", "Test", "Tester", "pw");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);
        userService.create(user5);
        userService.create(user6);

        Project project1 = new Project("P00754", "KiBucDu Final (Phase II)");
        Project project2 = new Project("P00843", "IT-/Prozessharmonisierung im Handel");
        Project project3 = new Project("P00998", "Bosch - ST-IPP");
        Project project4 = new Project("P01110", "Glory Times");
        Project project5 = new Project("P01244", "Phoenix Classic");
        projectService.create(project1);
        projectService.create(project2);
        projectService.create(project3);
        projectService.create(project4);
        projectService.create(project5);

        userService.assignUserToProject(user1, project1);
        userService.assignUserToProject(user2, project1);
        userService.assignUserToProject(user2, project2);

    }

    @Test
    @InSequence(2)
    public void testGetAllUsers() {
        Assert.assertEquals(6, userService.findAll().size());
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

    @Test
    @InSequence(4)
    public void testCreateUserWhichExists() {

        String identifier = "new identifier";

        User u = new User(identifier, null, null, null);

        userService.create(u);

        try {
            userService.create(u);
            Assert.fail("Unique constraint has to prohibit the creation");
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ElementAlreadyExistsRuntimeException) {
                return;
            }
            Assert.fail("Unexpected exception: " + ex);
        }

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
    public void testUpdateUser() throws ElementAlreadyExistsRuntimeException, NoSuchElementRuntimeException {

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

    @Test
    @InSequence(8)
    public void testUpdateUserExistingIdentifier() {

        String identifier = "aeich";
        String newIdentifier = "khansen";

        User u = userService.get(identifier);
        Assert.assertNotNull(u);

        u.setIdentifier(newIdentifier);

        try {
            userService.update(u);
            Assert.fail("Changing to an already present identifier should not be possible");
        } catch (EJBException ex) {
            if (ex.getCause() instanceof ElementAlreadyExistsRuntimeException) {
                return;
            }
            Assert.fail("Unexpected exception: " + ex);
        }

    }

    @Test
    @InSequence(9)
    public void testUpdateUserNonExistingNode() {

        User u = new User("unknow", null, null, null);

        try {
            userService.update(u);
            Assert.fail("Changing an not present element should not be possible");
        } catch (EJBException ex) {
            if (ex.getCause() instanceof NoSuchElementRuntimeException) {
                return;
            }
            Assert.fail("Unexpected exception: " + ex);
        }
    }

    @Test
    @InSequence(10)
    public void testDeleteUser() throws NoSuchElementRuntimeException {

        String identifier = "rvoeller";

        User u = userService.get(identifier);

        Assert.assertNotNull(u.getId());
        Assert.assertEquals(identifier, u.getIdentifier());

        userService.delete(identifier);

        Assert.assertNull(userService.get(identifier));

    }

    @Test
    @InSequence(11)
    public void testDeleteUserNoSuchUser() {

        String identifier = "P01244";

        User u = userService.get(identifier);

        Assert.assertNull(u);

        try {
            userService.delete(identifier);
            Assert.fail("Node should not be present any more");
        } catch (EJBException ex) {
            if (ex.getCause() instanceof NoSuchElementRuntimeException) {
                return;
            }
            Assert.fail("Unexpected exception: " + ex);
        }

    }

    @Test
    @InSequence(12)
    public void testFindAllAssignedToProject() throws NoSuchElementRuntimeException {

        Project project1 = projectService.get("P00754");
        Project project2 = projectService.get("P00843");
        Project project3 = projectService.get("P00998");

        Assert.assertEquals(2, userService.findAllAssignedToProject(project1).size());
        List<User> list = userService.findAllAssignedToProject(project2);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project3).size());

        Assert.assertEquals("aeich", list.get(0).getIdentifier());
    }

    @Test
    @InSequence(13)
    public void testAssignUserToProject() throws NoSuchElementRuntimeException {

        Project project3 = projectService.get("P00998");
        Project project4 = projectService.get("P01110");
        User user6 = userService.get("ttester");
        User user4 = userService.get("bborg");

        Assert.assertEquals(0, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project4).size());

        userService.assignUserToProject(user6, project3);
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(1, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project4).size());

        userService.assignUserToProject(user4, project3);
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(2, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project4).size());

        userService.assignUserToProject(user6, project4);
        Assert.assertEquals(2, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(2, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(1, userService.findAllAssignedToProject(project4).size());

    }

    @Test
    @InSequence(14)
    public void testAssignUserToProjectAlreadyUnassigned() throws NoSuchElementRuntimeException {

        Project project5 = projectService.get("P01244");
        User user5 = userService.get("hmeiser");

        Assert.assertEquals(0, projectService.findAllAssignedToUser(user5).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project5).size());
        userService.assignUserToProject(user5, project5);

        Assert.assertEquals(1, projectService.findAllAssignedToUser(user5).size());
        Assert.assertEquals(1, userService.findAllAssignedToProject(project5).size());

        userService.assignUserToProject(user5, project5);
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user5).size());
        Assert.assertEquals(1, userService.findAllAssignedToProject(project5).size());

    }

    @Test
    @InSequence(15)
    public void testUnassignUserFromProject() throws NoSuchElementRuntimeException {

        Project project3 = projectService.get("P00998");
        Project project4 = projectService.get("P01110");
        User user6 = userService.get("ttester");
        User user4 = userService.get("bborg");

        Assert.assertEquals(2, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(2, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(1, userService.findAllAssignedToProject(project4).size());

        userService.unassignUserFromProject(user6, project3);
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(1, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(1, userService.findAllAssignedToProject(project4).size());

        userService.unassignUserFromProject(user6, project4);
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(1, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project4).size());

        userService.unassignUserFromProject(user4, project3);
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project4).size());

    }

    @Test
    @InSequence(16)
    public void testUnassignUserFromProjectAlreadyUnassigned() throws NoSuchElementRuntimeException {

        Project project3 = projectService.get("P00998");
        Project project4 = projectService.get("P01110");
        User user6 = userService.get("ttester");
        User user4 = userService.get("bborg");

        Assert.assertEquals(0, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project4).size());

        userService.unassignUserFromProject(user4, project3);
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user6).size());
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user4).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project3).size());
        Assert.assertEquals(0, userService.findAllAssignedToProject(project4).size());

    }

}
