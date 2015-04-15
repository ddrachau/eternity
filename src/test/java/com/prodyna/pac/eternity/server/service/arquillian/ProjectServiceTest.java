package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.exception.technical.ElementAlreadyExistsRuntimeException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
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
public class ProjectServiceTest extends AbstractArquillianTest {

    @Inject
    private CypherService cypherService;

    @Inject
    private ProjectService projectService;

    @Inject
    private UserService userService;

    @Test
    @InSequence(1)
    public void createDemoData() {

        // clean DB from nodes and relations
        cypherService.query("MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r", null);

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
        projectService.create(new Project("P01140", "Liferay Unterstützung"));

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", null, "pw2");
        User user3 = new User("rvoeller", "Rudi", "Völler", "pw3");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        userService.assignUserToProject(user1, project1);
        userService.assignUserToProject(user1, project2);
        userService.assignUserToProject(user2, project1);

    }

    @Test
    @InSequence(2)
    public void testGetAllProjects() {
        Assert.assertEquals(6, projectService.findAll().size());
    }

    @Test
    @InSequence(3)
    public void testCreateProject() throws Exception {

        String identifier = "new Project";
        String description = "new description";

        Project p = new Project(identifier, description);

        Assert.assertNull(p.getId());

        Project p2 = projectService.create(p);

        Assert.assertSame(p, p2);
        Assert.assertNotNull(p2.getId());
        Assert.assertEquals(identifier, p2.getIdentifier());
        Assert.assertEquals(description, p2.getDescription());

    }

    @Test
    @InSequence(4)
    public void testCreateProjectWhichExists() throws ElementAlreadyExistsRuntimeException {

        String identifier = "new Project2";
        String description = "new description2";

        Project p = new Project(identifier, description);

        projectService.create(p);

        try {
            projectService.create(p);
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
    public void testGetProject() {

        String identifier = "P00754";
        String description = "KiBucDu Final (Phase II)";

        Project p = projectService.get(identifier);
        Assert.assertNotNull(p);
        Assert.assertEquals(identifier, p.getIdentifier());
        Assert.assertEquals(description, p.getDescription());

    }

    @Test
    @InSequence(6)
    public void testGetProjectUnknown() {

        Assert.assertNull(projectService.get("unknownId"));

    }

    @Test
    @InSequence(7)
    public void testUpdateProject() throws ElementAlreadyExistsRuntimeException, NoSuchElementRuntimeException {

        String identifier = "P00754";
        String description = "KiBucDu Final (Phase II)";
        String newDescription = "demo";

        Project p = projectService.get(identifier);
        Assert.assertNotNull(p);

        Assert.assertEquals(description, p.getDescription());
        p.setDescription(newDescription);

        p = projectService.update(p);

        Assert.assertNotNull(p);
        Assert.assertEquals(newDescription, p.getDescription());

        p = projectService.get(identifier);

        Assert.assertNotNull(p);
        Assert.assertEquals(newDescription, p.getDescription());

    }

    @Test
    @InSequence(8)
    public void testUpdateProjectExistingIdentifier() throws ElementAlreadyExistsRuntimeException, NoSuchElementRuntimeException {

        String identifier = "P00754";
        String newIdentifier = "P00843";

        Project p = projectService.get(identifier);
        Assert.assertNotNull(p);

        p.setIdentifier(newIdentifier);


        try {
            projectService.update(p);
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
    public void testUpdateProjectNonExistingNode() throws ElementAlreadyExistsRuntimeException, NoSuchElementRuntimeException {

        Project p = new Project("unknow", "P00755", "desc");

        try {
            projectService.update(p);
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
    public void testDeleteProject() throws NoSuchElementRuntimeException {

        String identifier = "P01244";

        Project p = projectService.get(identifier);

        Assert.assertNotNull(p.getId());
        Assert.assertEquals(identifier, p.getIdentifier());

        projectService.delete(identifier);

        Assert.assertNull(projectService.get(identifier));

    }

    @Test
    @InSequence(11)
    public void testDeleteProjectNoSuchProject() {

        String identifier = "P01244";

        Project p = projectService.get(identifier);

        Assert.assertNull(p);

        try {
            projectService.delete(identifier);
            Assert.fail("Node should not be present any more");
        } catch (EJBException ex) {
            if (ex.getCause() instanceof NoSuchElementRuntimeException) {
                return;
            }
            Assert.fail("Unexpected exception: " + ex);
        }

    }

    @Test()
    @InSequence(12)
    public void testFindAllAssignedUser() {

        User user1 = userService.get("khansen");
        User user2 = userService.get("aeich");
        User user3 = userService.get("rvoeller");

        Assert.assertEquals(2, projectService.findAllAssignedToUser(user1).size());
        List<Project> list = projectService.findAllAssignedToUser(user2);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user3).size());

        Assert.assertEquals("P00754", list.get(0).getIdentifier());

    }

}
