package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.components.common.DateUtils;
import com.prodyna.pac.eternity.server.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.server.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.server.model.booking.Booking;
import com.prodyna.pac.eternity.server.model.project.Project;
import com.prodyna.pac.eternity.server.model.user.User;
import com.prodyna.pac.eternity.server.service.booking.BookingService;
import com.prodyna.pac.eternity.server.service.project.ProjectService;
import com.prodyna.pac.eternity.server.service.user.UserService;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;


@RunWith(Arquillian.class)
public class BookingServiceTest extends AbstractArquillianTest {

    @Inject
    private CypherService cypherService;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Inject
    private BookingService bookingService;

    @Test
    @InSequence(1)
    public void createDemoData() throws DuplicateTimeBookingException, UserNotAssignedToProjectException, InvalidBookingException, ElementAlreadyExistsException {

        // clean DB from nodes and relations
        cypherService.query(CLEANUP_QUERY, null);

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", null, "pw2");
        User user3 = new User("rvoeller", "Rudi", "Völler", null);
        User user4 = new User("bborg", "Björn", "Borg", "pw");
        User user5 = new User("hmeiser", "Hans", "Meiser", "pw");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);
        userService.create(user5);

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
        userService.assignUserToProject(user3, project3);
        userService.assignUserToProject(user5, project5);

        Booking booking1 = new Booking(DateUtils.getCalendar(2015, 3, 2, 10, 0), DateUtils.getCalendar(2015, 3, 2, 14, 0), 5);
        Booking booking2 = new Booking(DateUtils.getCalendar(2015, 3, 3, 9, 0), DateUtils.getCalendar(2015, 3, 3, 14, 30), 15, "work1");
        Booking booking3 = new Booking(DateUtils.getCalendar(2015, 3, 5, 9, 0), DateUtils.getCalendar(2015, 3, 5, 18, 10), 60);
        Booking booking4 = new Booking(DateUtils.getCalendar(2015, 3, 6, 10, 0), DateUtils.getCalendar(2015, 3, 6, 15, 0), 30, "work2");
        Booking booking5 = new Booking(DateUtils.getCalendar(2015, 3, 7, 10, 0), DateUtils.getCalendar(2015, 3, 7, 16, 0), 45);
        Booking booking6 = new Booking(DateUtils.getCalendar(2015, 3, 7, 10, 0), DateUtils.getCalendar(2015, 3, 7, 16, 0), 45, "work3");

        bookingService.create(booking1, user1, project1);
        bookingService.create(booking2, user1, project1);
        bookingService.create(booking3, user2, project1);
        bookingService.create(booking4, user2, project2);
        bookingService.create(booking5, user2, project2);
        bookingService.create(booking6, user5, project5);

    }

    @Test
    @InSequence(2)
    public void testGet() {

        User user1 = userService.get("khansen");
        List<Booking> bookings = bookingService.findByUser(user1);

        Assert.assertEquals(2, bookings.size());

        Booking b = bookings.get(0);

        Assert.assertNotNull(b);
        Assert.assertNotNull(b.getId());
        Assert.assertEquals(b, bookingService.get(b.getId()));
        Assert.assertNull(bookingService.get("unknown"));

    }

    @Test
    @InSequence(3)
    public void testFindByUser() {

        User user1 = userService.get("khansen");
        User user2 = userService.get("aeich");
        User user3 = userService.get("rvoeller");

        Assert.assertEquals(2, bookingService.findByUser(user1).size());
        Assert.assertEquals(3, bookingService.findByUser(user2).size());
        Assert.assertEquals(0, bookingService.findByUser(user3).size());

    }

    @Test
    @InSequence(4)
    public void testFindByProject() {

        Project project1 = projectService.get("P00754");
        Project project2 = projectService.get("P00843");
        Project project3 = projectService.get("P00998");

        Assert.assertEquals(3, bookingService.findByProject(project1).size());
        Assert.assertEquals(2, bookingService.findByProject(project2).size());
        Assert.assertEquals(0, bookingService.findByProject(project3).size());

    }

    @Test
    @InSequence(5)
    public void testFindByUserAndProject() {

        Project project1 = projectService.get("P00754");
        Project project2 = projectService.get("P00843");
        Project project3 = projectService.get("P00998");
        Project projectU = new Project("unknonw", null);
        User user1 = userService.get("khansen");
        User user2 = userService.get("aeich");
        User user3 = userService.get("rvoeller");
        User userU = new User("unknown", null, null, null);

        Assert.assertEquals(2, bookingService.findByUserAndProject(user1, project1).size());
        Assert.assertEquals(1, bookingService.findByUserAndProject(user2, project1).size());
        Assert.assertEquals(0, bookingService.findByUserAndProject(user3, project1).size());

        Assert.assertEquals(0, bookingService.findByUserAndProject(user1, project2).size());
        Assert.assertEquals(2, bookingService.findByUserAndProject(user2, project2).size());
        Assert.assertEquals(0, bookingService.findByUserAndProject(user3, project2).size());

        Assert.assertEquals(0, bookingService.findByUserAndProject(user1, project3).size());

        Assert.assertEquals(0, bookingService.findByUserAndProject(userU, project1).size());
        Assert.assertEquals(0, bookingService.findByUserAndProject(user1, projectU).size());
        Assert.assertEquals(0, bookingService.findByUserAndProject(userU, projectU).size());

    }

    @Test
    @InSequence(6)
    public void testCreate() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException {

        Project project3 = projectService.get("P00998");
        User user3 = userService.get("rvoeller");

        Assert.assertTrue(userService.isAssignedTo(user3, project3));

        Assert.assertEquals(0, bookingService.findByUserAndProject(user3, project3).size());
        Assert.assertEquals(0, bookingService.findByUser(user3).size());
        Assert.assertEquals(0, bookingService.findByProject(project3).size());

        String description = "desc";

        Booking b = new Booking(DateUtils.getCalendar(2015, 3, 10, 10, 0), DateUtils.getCalendar(2015, 3, 10, 16, 0), 45, description);
        Assert.assertNull(b.getId());

        Booking createdB = bookingService.create(b, user3, project3);
        Assert.assertNotNull(createdB.getId());
        Assert.assertEquals(b, createdB);
        Assert.assertEquals(1, bookingService.findByUserAndProject(user3, project3).size());
        Assert.assertEquals(1, bookingService.findByUser(user3).size());
        Assert.assertEquals(1, bookingService.findByProject(project3).size());
        Assert.assertEquals(description, createdB.getDescription());

        Booking secondB = new Booking(DateUtils.getCalendar(2015, 3, 11, 10, 0), DateUtils.getCalendar(2015, 3, 11, 16, 0), 45);
        bookingService.create(secondB, user3, project3);
        Assert.assertEquals(2, bookingService.findByUserAndProject(user3, project3).size());
        Assert.assertEquals(2, bookingService.findByUser(user3).size());
        Assert.assertEquals(2, bookingService.findByProject(project3).size());

    }

    @Test(expected = UserNotAssignedToProjectException.class)
    @InSequence(7)
    public void testCreateUnassignedUser() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException {

        Project project2 = projectService.get("P00843");
        User user3 = userService.get("rvoeller");

        Assert.assertFalse(userService.isAssignedTo(user3, project2));
        Booking b = new Booking(DateUtils.getCalendar(2015, 3, 10, 10, 0), DateUtils.getCalendar(2015, 3, 10, 16, 0), 45);

        bookingService.create(b, user3, project2);

    }

    @Test
    @InSequence(8)
    public void testCreateDuplicateBooking() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException {

        Project project4 = projectService.get("P01110");
        User user4 = userService.get("bborg");

        userService.assignUserToProject(user4, project4);
        Booking b = new Booking(DateUtils.getCalendar(2015, 3, 10, 10, 0), DateUtils.getCalendar(2015, 3, 10, 16, 0), 45);
        bookingService.create(b, user4, project4);

        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 11, 0), DateUtils.getCalendar(2015, 3, 10, 15, 0), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 0, 0), DateUtils.getCalendar(2015, 3, 10, 15, 0), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 11, 0), DateUtils.getCalendar(2015, 3, 10, 16, 0), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 10, 0), DateUtils.getCalendar(2015, 3, 10, 16, 0), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 8, 0), DateUtils.getCalendar(2015, 3, 10, 10, 30), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 12, 0), DateUtils.getCalendar(2015, 3, 10, 17, 0), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 9, 0), DateUtils.getCalendar(2015, 3, 10, 18, 0), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 10, 0), DateUtils.getCalendar(2015, 3, 10, 18, 0), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 9, 0), DateUtils.getCalendar(2015, 3, 10, 16, 0), 0), user4, project4);
            Assert.fail("Booking with overlapping time is not allowed");
        } catch (DuplicateTimeBookingException e) {
            // expected
        }

        //good
        bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 8, 0), DateUtils.getCalendar(2015, 3, 10, 10, 0), 0), user4, project4);
        bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 16, 0), DateUtils.getCalendar(2015, 3, 10, 17, 0), 0), user4, project4);

    }


    @Test
    @InSequence(9)
    public void testCreateInvalidBooking() throws DuplicateTimeBookingException, UserNotAssignedToProjectException {

        Project project4 = projectService.get("P01110");
        User user4 = userService.get("bborg");

        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 10, 0), DateUtils.getCalendar(2015, 3, 9, 16, 0), 45), user4, project4);
            Assert.fail("Second date before first");
        } catch (InvalidBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 10, 0), DateUtils.getCalendar(2015, 3, 10, 9, 0), 45), user4, project4);
            Assert.fail("Second date before first");
        } catch (InvalidBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 11, 0), DateUtils.getCalendar(2015, 3, 10, 13, 0), 180), user4, project4);
            Assert.fail("Break too long");
        } catch (InvalidBookingException e) {
            // expected
        }
        try {
            bookingService.create(new Booking(DateUtils.getCalendar(2015, 3, 10, 10, 0), DateUtils.getCalendar(2015, 3, 11, 16, 0), 45), user4, project4);
            Assert.fail("Second date different day");
        } catch (InvalidBookingException e) {
            // expected
        }

    }

    @Test
    @InSequence(10)
    public void testUpdate() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException {

        Project project4 = projectService.get("P01110");
        User user4 = userService.get("bborg");

        Assert.assertTrue(userService.isAssignedTo(user4, project4));

        int startHour = 10;
        int startMinute = 0;
        int endHour = 16;
        int endMinute = 0;
        int newStartHour = 11;
        int newStartMinute = 30;
        int newEndHour = 15;
        int newEndMinute = 40;

        Booking b = new Booking(DateUtils.getCalendar(2015, 4, 10, startHour, startMinute), DateUtils.getCalendar(2015, 4, 10, endHour, endMinute), 45);
        b = bookingService.create(b, user4, project4);
        Assert.assertNotNull(b.getId());

        Calendar cal1 = b.getStartTime();
        Calendar cal2 = b.getEndTime();

        Assert.assertEquals(startHour, cal1.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(startMinute, cal1.get(Calendar.MINUTE));
        Assert.assertEquals(endHour, cal2.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(endMinute, cal2.get(Calendar.MINUTE));
        Assert.assertNull(b.getDescription());

        b.setStartTime(DateUtils.getCalendar(2015, 4, 11, newStartHour, newStartMinute));
        b.setEndTime(DateUtils.getCalendar(2015, 4, 11, newEndHour, newEndMinute));
        b.setDescription("demo");

        b = bookingService.update(b);

        Calendar cal3 = b.getStartTime();
        Calendar cal4 = b.getEndTime();

        Assert.assertEquals(newStartHour, cal3.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(newStartMinute, cal3.get(Calendar.MINUTE));
        Assert.assertEquals(newEndHour, cal4.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(newEndMinute, cal4.get(Calendar.MINUTE));
        Assert.assertNotNull(b.getDescription());

        b = bookingService.get(b.getId());

        Calendar cal5 = b.getStartTime();
        Calendar cal6 = b.getEndTime();

        Assert.assertEquals(newStartHour, cal5.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(newStartMinute, cal5.get(Calendar.MINUTE));
        Assert.assertEquals(newEndHour, cal6.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(newEndMinute, cal6.get(Calendar.MINUTE));
        Assert.assertNotNull(b.getDescription());

    }

    @Test
    @InSequence(11)
    public void testUpdateWithInvalidBooking() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException {

        Project project4 = projectService.get("P01110");
        User user4 = userService.get("bborg");

        Assert.assertTrue(userService.isAssignedTo(user4, project4));

        Booking b = new Booking(DateUtils.getCalendar(2015, 5, 12, 9, 30), DateUtils.getCalendar(2015, 5, 12, 14, 30), 45);
        b = bookingService.create(b, user4, project4);
        Assert.assertNotNull(b.getId());

        try {
            Calendar s = b.getStartTime();
            b.setStartTime(b.getEndTime());
            b.setEndTime(s);
            bookingService.update(b);
            Assert.fail("Second date before first");
        } catch (InvalidBookingException e) {
            // expected
        }
        try {
            b = bookingService.get(b.getId());
            b.setBreakDuration(6 * 60);
            bookingService.update(b);
            Assert.fail("Break too long");
        } catch (InvalidBookingException e) {
            // expected
        }
        try {
            b = bookingService.get(b.getId());
            b.setEndTime(DateUtils.getCalendar(2015, 5, 13, 12, 0));
            bookingService.update(b);
            Assert.fail("Second date different day");
        } catch (InvalidBookingException e) {
            // expected
        }

    }

    @Test(expected = DuplicateTimeBookingException.class)
    @InSequence(12)
    public void testUpdateWithDuplicateBooking() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException {

        Project project4 = projectService.get("P01110");
        User user4 = userService.get("bborg");

        Assert.assertTrue(userService.isAssignedTo(user4, project4));

        bookingService.create(new Booking(DateUtils.getCalendar(2015, 6, 12, 9, 30), DateUtils.getCalendar(2015, 6, 12, 14, 30), 45), user4, project4);
        Booking b = bookingService.create(new Booking(DateUtils.getCalendar(2015, 6, 12, 15, 0), DateUtils.getCalendar(2015, 6, 12, 18, 0), 0), user4, project4);
        Assert.assertNotNull(b.getId());

        b.setStartTime(DateUtils.getCalendar(2015, 6, 12, 13, 0));
        bookingService.update(b);

        Assert.fail("Update not valid, overlapping times");

    }

    @Test
    @InSequence(13)
    public void testDelete() {

        Project project5 = projectService.get("P01244");
        User user5 = userService.get("hmeiser");

        Assert.assertTrue(userService.isAssignedTo(user5, project5));

        List<Booking> byProject = bookingService.findByProject(project5);
        List<Booking> byUser = bookingService.findByUser(user5);

        Assert.assertEquals(1, byProject.size());
        Assert.assertEquals(1, byUser.size());
        Assert.assertNotNull(byProject.get(0));
        Assert.assertEquals(byProject.get(0), byUser.get(0));

        Booking b = byProject.get(0);

        Assert.assertNotNull(bookingService.get(b.getId()));

        bookingService.delete(b.getId());

        Assert.assertNull(bookingService.get(b.getId()));
        Assert.assertTrue(bookingService.findByProject(project5).isEmpty());
        Assert.assertTrue(bookingService.findByUser(user5).isEmpty());

    }

}


