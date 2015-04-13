package com.prodyna.pac.eternity.server.arquillian;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.ProjectService;
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
public class ProjectServiceTest {

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "eternity-arq.jar").addPackages(true, "com.prodyna.pac");
        jar.addAsResource("META-INF/beans.xml");
        jar.addAsResource("META-INF/persistence.xml");
        System.out.println(jar.toString(true));
        return jar;
    }

    @Inject
    private ProjectService projectService;

    @Inject
    private CypherService cypherService;

    @Test
    @InSequence(1)
    public void createDemoData() throws Exception {

        // clean DB from nodes and relations
        cypherService.query("MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r", null);

        projectService.create(new Project("P00754", "KiBucDu Final (Phase II)"));
        projectService.create(new Project("P00843", "IT-/Prozessharmonisierung im Handel"));
        projectService.create(new Project("P00998", "Bosch - ST-IPP"));
        projectService.create(new Project("P01110", "Glory Times"));
        projectService.create(new Project("P01244", "Phoenix Classic"));
        projectService.create(new Project("P01140", "Liferay Unterstützung"));

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

    @Test(expected = ElementAlreadyExistsException.class)
    @InSequence(4)
    public void testCreateProjectWhichExists() throws ElementAlreadyExistsException {

        String identifier = "new Project2";
        String description = "new description2";

        Project p = new Project(identifier, description);

        projectService.create(p);
        projectService.create(p);
        Assert.fail("Unique constraint has to prohibit the creation");

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
    public void testUpdateProject() throws ElementAlreadyExistsException, NoSuchElementException {

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

    @Test(expected = ElementAlreadyExistsException.class)
    @InSequence(8)
    public void testUpdateProjectExistingIdentifier() throws ElementAlreadyExistsException, NoSuchElementException {

        String identifier = "P00754";
        String newIdentifier = "P00843";

        Project p = projectService.get(identifier);
        Assert.assertNotNull(p);

        p.setIdentifier(newIdentifier);

        projectService.update(p);

        Assert.fail("Changing to an already present identifier should not be possible");

    }

    @Test(expected = NoSuchElementException.class)
    @InSequence(9)
    public void testUpdateProjectNonExistingNode() throws ElementAlreadyExistsException, NoSuchElementException {

        Project p = new Project("unknow", "P00755", "desc");
        projectService.update(p);

        Assert.fail("Changing an not present element should not be possible");

    }

    @Test
    @InSequence(10)
    public void testDeleteProject() throws NoSuchElementException {

        String identifier = "P01244";

        Project p = projectService.get(identifier);

        Assert.assertNotNull(p.getId());
        Assert.assertEquals(identifier, p.getIdentifier());

        projectService.delete(identifier);

        Assert.assertNull(projectService.get(identifier));

    }

    @Test(expected = NoSuchElementException.class)
    @InSequence(11)
    public void testDeleteProjectNoSuchProject() throws NoSuchElementException {

        String identifier = "P01244";

        Project p = projectService.get(identifier);

        Assert.assertNull(p);

        projectService.delete(identifier);
        Assert.fail("Node should not be present any more");

    }

    @Test()
    @InSequence(12)
    public void testFindAllAssignedUser() {

        Assert.fail("not implemented");

    }

}
