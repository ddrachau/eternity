package com.prodyna.pac.eternity.test.server.service;

import com.prodyna.pac.eternity.components.common.DateUtils;
import com.prodyna.pac.eternity.components.common.RememberMeUtils;
import com.prodyna.pac.eternity.test.helper.AbstractArquillianTest;
import com.prodyna.pac.eternity.test.helper.DatabaseCleaner;
import com.prodyna.pac.eternity.server.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.model.authentication.Login;
import com.prodyna.pac.eternity.server.model.booking.Booking;
import com.prodyna.pac.eternity.server.model.project.Project;
import com.prodyna.pac.eternity.server.model.user.User;
import com.prodyna.pac.eternity.server.service.authentication.AuthenticationService;
import com.prodyna.pac.eternity.server.service.authentication.SessionService;
import com.prodyna.pac.eternity.server.service.booking.BookingService;
import com.prodyna.pac.eternity.server.service.project.ProjectService;
import com.prodyna.pac.eternity.server.service.user.UserService;
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
    private DatabaseCleaner databaseCleaner;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Inject
    private BookingService bookingService;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private SessionService sessionService;

    @Test
    @InSequence(1)
    public void createDemoData() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException, ElementAlreadyExistsException {

        databaseCleaner.deleteAllData();

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", null, "pw2");
        User user3 = new User("rvoeller", "Rudi", "Völler", "pw3");
        User user4 = new User("bborg", "Björn", "Borg", "pw");
        User user5 = new User("hmeiser", "Hans", "Meiser", "pw");
        User user6 = new User("ttester", "Test", "Tester", "pw");
        User user7 = new User("ttester2", "Test2", "Tester", "pw");
        User user8 = new User("ttester3", "Test3", "Tester", null);
        User user9 = new User("ttester4", "Test4", "Tester", "pw");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);
        userService.create(user5);
        userService.create(user6);
        userService.create(user7);
        userService.create(user8);
        userService.create(user9);

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

    @Test()
    @InSequence(2)
    public void testIsAssignedTo() {

        User user1 = userService.get("khansen");
        User user2 = userService.get("aeich");
        User user3 = userService.get("rvoeller");
        Project project1 = projectService.get("P00754");
        Project project2 = projectService.get("P00843");
        Project project3 = projectService.get("P00998");

        Assert.assertTrue(userService.isAssignedTo(user1, project1));
        Assert.assertTrue(userService.isAssignedTo(user2, project1));
        Assert.assertTrue(userService.isAssignedTo(user2, project2));

        Assert.assertFalse(userService.isAssignedTo(user1, project2));
        Assert.assertFalse(userService.isAssignedTo(user2, project3));
        Assert.assertFalse(userService.isAssignedTo(user3, project3));

    }

    @Test
    @InSequence(2)
    public void testGetAllUsers() {

        Assert.assertEquals(9, userService.findAll().size());
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
    public void testUpdateUser() throws ElementAlreadyExistsException, NoSuchElementRuntimeException {

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
    public void testUpdateUserExistingIdentifier() throws ElementAlreadyExistsException {

        String identifier = "aeich";
        String newIdentifier = "khansen";

        User u = userService.get(identifier);
        Assert.assertNotNull(u);

        u.setIdentifier(newIdentifier);

        userService.update(u);
        Assert.fail("Changing to an already present identifier should not be possible");

    }

    @Test
    @InSequence(9)
    public void testUpdateUserNonExistingNode() throws ElementAlreadyExistsException {

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

    @Test
    @InSequence(17)
    public void testGetForBooking() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException {

        User user5 = userService.get("hmeiser");
        Project project5 = projectService.get("P01244");

        userService.assignUserToProject(user5, project5);
        Booking booking5 = new Booking(DateUtils.getCalendar(2015, 3, 7, 10, 0), DateUtils.getCalendar(2015, 3, 7, 16, 0), 45);
        bookingService.create(booking5, user5, project5);

        Assert.assertEquals(user5, userService.getByBooking(booking5));

    }

    @Test
    @InSequence(18)
    public void testDeleteUserWithRelations() throws InvalidLoginException, InvalidBookingException,
            DuplicateTimeBookingException, UserNotAssignedToProjectException, ElementAlreadyExistsException {

        User user = userService.create(new User("mmon", "Mike", "Mon", "secret"));
        Project project = projectService.create(new Project("P01123", "DB All new", ""));
        userService.assignUserToProject(user, project);
        Booking booking = bookingService.create(
                new Booking(DateUtils.getCalendar(2015, 3, 3, 9, 0), DateUtils.getCalendar(2015, 3, 3, 14, 30), 15, "work1"), user, project);

        Assert.assertNotNull(user);
        Assert.assertNotNull(project);
        Assert.assertNotNull(booking);
        Assert.assertTrue(userService.isAssignedTo(user, project));

        Assert.assertEquals(1, bookingService.findByProject(project).size());
        Assert.assertEquals(1, bookingService.findByUser(user).size());

        userService.delete(user.getIdentifier());

        Assert.assertEquals(0, bookingService.findByProject(project).size());
        Assert.assertNull(userService.get(user.getIdentifier()));
        Assert.assertNotNull(projectService.get(project.getIdentifier()));

        user = userService.create(new User("mmon", "Mike", "Mon", "secret"));
        userService.assignUserToProject(user, project);
        Assert.assertTrue(userService.isAssignedTo(user, project));
        Assert.assertNotNull(userService.get(user.getIdentifier()));
        userService.delete(user.getIdentifier());
        Assert.assertNull(userService.get(user.getIdentifier()));

        user = userService.create(new User("mmon", "Mike", "Mon", "secret"));
        Login l = authenticationService.login(new Login(user.getIdentifier(), "secret"));
        Assert.assertNotNull(l);
        Assert.assertNotNull(sessionService.get(l.getXsrfToken()));

        userService.delete(user.getIdentifier());
        Assert.assertNull(userService.get(user.getIdentifier()));

    }

    @Test
    @InSequence(19)
    public void testStorePassword() throws InvalidUserException, InvalidPasswordException {

        String newPassword = "new";

        try {
            userService.checkIfPasswordIsValid("ttester2", newPassword);
            Assert.fail();
        } catch (InvalidPasswordException e) {
            //expected
        }
        userService.storePassword("ttester2", newPassword);
        userService.checkIfPasswordIsValid("ttester2", newPassword);

        userService.storePassword("ttester3", newPassword);
        userService.checkIfPasswordIsValid("ttester3", newPassword);

    }

    @Test(expected = InvalidUserException.class)
    @InSequence(20)
    public void testStorePasswordWithUnkownUser() throws InvalidUserException {

        User notValidUser = new User("unknown", "fore", "sur", "pw");

        userService.storePassword("unknown", "newPw");

    }

    @Test
    @InSequence(21)
    public void testChangePassword() throws InvalidLoginException {

        String newPassword = "boom";
        String oldPassword = "pw";

        userService.checkIfPasswordIsValid("ttester4", oldPassword);
        userService.changePassword("ttester4", oldPassword, newPassword);
        try {
            userService.checkIfPasswordIsValid("ttester4", oldPassword);
            Assert.fail();
        } catch (InvalidPasswordException e) {
            // expected
        }
        userService.checkIfPasswordIsValid("ttester4", newPassword);

    }

    @Test(expected = InvalidPasswordException.class)
    @InSequence(22)
    public void testChangeWithInvalidPassword() throws InvalidLoginException {

        userService.changePassword("khansen", "wrongpw", "newPass");

        Assert.fail("Cannot change password with an invalid password");

    }

    @Test(expected = InvalidUserException.class)
    @InSequence(23)
    public void testChangeWithUnknownUser() throws InvalidLoginException {

        userService.changePassword("unknown", "old", "newPw");
        Assert.fail("Cannot change password for an unknown user");

    }

    @Test
    @InSequence(24)
    public void testGetBySessionId() throws InvalidLoginException, ElementAlreadyExistsException {

        User user = userService.create(new User("gbsession", "Mike", "Mon", "secret"));
        Login l = authenticationService.login(new Login(user.getIdentifier(), "secret"));

        User user2 = userService.getBySessionId(l.getXsrfToken());
        Assert.assertNotNull(user);
        Assert.assertNotNull(user2);
        Assert.assertEquals(user, user2);

    }

    @Test
    @InSequence(25)
    public void testGetByRememberMe() throws InvalidLoginException, ElementAlreadyExistsException {

        User user = userService.create(new User("gbremember", "Mike", "Mon", "secret"));
        Login l = authenticationService.login(new Login(user.getIdentifier(), "secret", true));

        User user2 = userService.getByRememberMe(RememberMeUtils.getRememberMeId(l.getRememberMeToken()));
        Assert.assertNotNull(user);
        Assert.assertNotNull(user2);
        Assert.assertEquals(user, user2);

    }

}
