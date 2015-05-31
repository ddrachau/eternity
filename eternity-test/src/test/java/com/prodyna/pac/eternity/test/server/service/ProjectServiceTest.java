package com.prodyna.pac.eternity.test.server.service;

import com.prodyna.pac.eternity.booking.service.BookingService;
import com.prodyna.pac.eternity.common.helper.CalendarBuilder;
import com.prodyna.pac.eternity.common.model.FilterRequest;
import com.prodyna.pac.eternity.common.model.FilterResponse;
import com.prodyna.pac.eternity.common.model.booking.Booking;
import com.prodyna.pac.eternity.common.model.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.common.model.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.common.model.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.common.model.project.Project;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.project.service.ProjectService;
import com.prodyna.pac.eternity.test.helper.AbstractArquillianTest;
import com.prodyna.pac.eternity.test.helper.DatabaseCleaner;
import com.prodyna.pac.eternity.user.service.UserService;
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
    private DatabaseCleaner databaseCleaner;

    @Inject
    private ProjectService projectService;

    @Inject
    private UserService userService;

    @Inject
    private BookingService bookingService;

    @Inject
    private CalendarBuilder calendarBuilder;

    @Test
    @InSequence(1)
    public void createDemoData() throws ElementAlreadyExistsException {

        databaseCleaner.deleteAllData();

        Project project1 = new Project("P00754", "KiBucDu Final (Phase II)");
        Project project2 = new Project("P00843", "IT-/Prozessharmonisierung im Handel");
        Project project3 = new Project("P00998", "Bosch - ST-IPP");
        Project project4 = new Project("P01110", "Glory Times");
        Project project5 = new Project("P01244", "Phoenix Classic");
        Project project6 = new Project("P01140", "Liferay Unterstützung");
        projectService.create(project1);
        projectService.create(project2);
        projectService.create(project3);
        projectService.create(project4);
        projectService.create(project5);
        projectService.create(project6);

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", "A", "pw2");
        User user3 = new User("rvoeller", "Rudi", "Völler", "pw3");
        User user4 = new User("bborg", "Björn", "Borg", "pw");
        User user5 = new User("hmeiser", "Hans", "Meiser", "pw");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);
        userService.create(user5);

        userService.assignUserToProject(user1, project1);
        userService.assignUserToProject(user1, project2);
        userService.assignUserToProject(user2, project1);

    }


    @Test
    @InSequence(3)
    public void testGetAllProjects() {

        Assert.assertEquals(6, projectService.find().size());
    }

    @Test
    @InSequence(4)
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
    @InSequence(5)
    public void testCreateProjectWhichExists() throws ElementAlreadyExistsException {

        String identifier = "new Project2";
        String description = "new description2";

        Project p = new Project(identifier, description);

        projectService.create(p);

        projectService.create(p);
        Assert.fail("Unique constraint has to prohibit the creation");

    }

    @Test
    @InSequence(6)
    public void testGetProject() {

        String identifier = "P00754";
        String description = "KiBucDu Final (Phase II)";

        Project p = projectService.get(identifier);
        Assert.assertNotNull(p);
        Assert.assertEquals(identifier, p.getIdentifier());
        Assert.assertEquals(description, p.getDescription());

    }

    @Test
    @InSequence(7)
    public void testGetProjectUnknown() {

        Assert.assertNull(projectService.get("unknownId"));

    }

    @Test
    @InSequence(8)
    public void testUpdateProject() throws ElementAlreadyExistsException, NoSuchElementRuntimeException {

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
    @InSequence(9)
    public void testUpdateProjectExistingIdentifier() throws ElementAlreadyExistsException, NoSuchElementRuntimeException {

        String identifier = "P00754";
        String newIdentifier = "P00843";

        Project p = projectService.get(identifier);
        Assert.assertNotNull(p);

        p.setIdentifier(newIdentifier);

        projectService.update(p);
        Assert.fail("Changing to an already present identifier should not be possible");

    }

    @Test
    @InSequence(10)
    public void testUpdateProjectNonExistingNode() throws ElementAlreadyExistsException, NoSuchElementRuntimeException {

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
    @InSequence(11)
    public void testDeleteProject() throws NoSuchElementRuntimeException {

        String identifier = "P01244";

        Project p = projectService.get(identifier);

        Assert.assertNotNull(p.getId());
        Assert.assertEquals(identifier, p.getIdentifier());

        projectService.delete(identifier);

        Assert.assertNull(projectService.get(identifier));

    }

    @Test
    @InSequence(12)
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
    @InSequence(13)
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

    @Test
    @InSequence(14)
    public void testGetForBooking() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException {

        User user5 = userService.get("hmeiser");
        Project project4 = projectService.get("P01110");

        userService.assignUserToProject(user5, project4);
        Booking booking5 = new Booking(calendarBuilder.getCalendar(2015, 3, 7, 10, 0),
                calendarBuilder.getCalendar(2015, 3, 7, 16, 0), 45);
        bookingService.create(booking5, user5, project4);

        Assert.assertEquals(project4, projectService.get(booking5));

    }

    @Test
    @InSequence(15)
    public void testDeleteProjectWithRelations() throws NoSuchElementRuntimeException, InvalidBookingException,
            DuplicateTimeBookingException, UserNotAssignedToProjectException, ElementAlreadyExistsException {

        User user = userService.create(new User("mmon", "Mike", "Mon", "secret"));
        Project project = projectService.create(new Project("P01123", "DB All new", ""));
        userService.assignUserToProject(user, project);
        Booking booking = bookingService.create(
                new Booking(calendarBuilder.getCalendar(2015, 3, 3, 9, 0),
                        calendarBuilder.getCalendar(2015, 3, 3, 14, 30), 15, "work1"), user, project);

        Assert.assertNotNull(user);
        Assert.assertNotNull(project);
        Assert.assertNotNull(booking);
        Assert.assertTrue(userService.isAssignedTo(user, project));

        Assert.assertEquals(1, bookingService.findByProject(project).size());
        Assert.assertEquals(1, bookingService.findByUser(user).size());

        projectService.delete(project.getIdentifier());

        Assert.assertEquals(0, bookingService.findByUser(user).size());
        Assert.assertNull(projectService.get(project.getIdentifier()));
        Assert.assertNotNull(userService.get(user.getIdentifier()));

        project = projectService.create(new Project("P01123", "DB All new", ""));
        userService.assignUserToProject(user, project);
        Assert.assertTrue(userService.isAssignedTo(user, project));
        Assert.assertNotNull(projectService.get(project.getIdentifier()));
        projectService.delete(project.getIdentifier());
        Assert.assertNull(projectService.get(project.getIdentifier()));

    }

    @Test
    @InSequence(16)
    public void testFindAssignProject() throws ElementAlreadyExistsException {

        User user = userService.create(new User("idfindassign", "AB", "CD", "pw"));

        int size = projectService.find().size();
        Assert.assertTrue(size > 0);
        Assert.assertEquals(0, projectService.findAllAssignedToUser(user).size());
        List<Project> allAssignableToUser = projectService.findAllAssignableToUser(user);
        Assert.assertEquals(size, allAssignableToUser.size());
        userService.assignUserToProject(user, allAssignableToUser.get(0));
        Assert.assertEquals(1, projectService.findAllAssignedToUser(user).size());
        Assert.assertEquals(size - 1, projectService.findAllAssignableToUser(user).size());

    }

    @Test
    @InSequence(17)
    public void testFindByFilter() throws ElementAlreadyExistsException {

        createDemoData();

        FilterResponse<Project> response = projectService.find(new FilterRequest(null, null, 0, 0));

        Assert.assertEquals(6, response.getTotalSize());
        Assert.assertEquals(6, response.getData().size());

        response = projectService.find(new FilterRequest(null, null, 2, 0));

        Assert.assertEquals(6, response.getTotalSize());
        Assert.assertEquals(4, response.getData().size());

        response = projectService.find(new FilterRequest(null, null, 0, 4));

        Assert.assertEquals(6, response.getTotalSize());
        Assert.assertEquals(4, response.getData().size());

        response = projectService.find(new FilterRequest(null, null, 4, 4));

        Assert.assertEquals(6, response.getTotalSize());
        Assert.assertEquals(2, response.getData().size());

        response = projectService.find(new FilterRequest("+identifier", null, 0, 0));

        Assert.assertEquals("P00754", response.getData().get(0).getIdentifier());

        response = projectService.find(new FilterRequest("-identifier", null, 0, 0));

        Assert.assertEquals("P01244", response.getData().get(0).getIdentifier());

        response = projectService.find(new FilterRequest(null, new String[]{"description:Bosch"}, 0, 0));

        Assert.assertEquals(1, response.getTotalSize());
        Assert.assertEquals(1, response.getData().size());

    }

    @Test
    @InSequence(18)
    public void testCreateProjectWithoutIdentifier() throws Exception {

        String identifier = "";
        String description = "Dirk";

        Project p = new Project(identifier, description);

        try {
            projectService.create(p);
            Assert.fail("invalid project");
        } catch (RuntimeException e) {
            // expected
        }

        p.setIdentifier(null);

        try {
            projectService.create(p);
            Assert.fail("invalid project");
        } catch (RuntimeException e) {
            // expected
        }

    }

}
